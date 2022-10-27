package com.icommerce.service.impl;


import com.icommerce.dto.CartDto;
import com.icommerce.entity.Cart;
import com.icommerce.entity.Product;
import com.icommerce.exception.BadRequestException;
import com.icommerce.exception.DataPersitException;
import com.icommerce.exception.NotPermissionException;
import com.icommerce.exception.ProductIsNotEnoughException;
import com.icommerce.mapper.ProductMapper;
import com.icommerce.repository.CartRepository;
import com.icommerce.repository.ProductRepository;
import com.icommerce.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;

    @Override
    public Page<CartDto> getCarts(Pageable pageable) {
        Page<Cart> cartPage = cartRepository.findAll(pageable);
        return cartPage.map(productMapper::cartToCartDto);
    }

    @Override
    public List<CartDto> getCartsByUser(String userId) {
        List<Cart> cartPage = cartRepository.findByUserId(userId).orElseGet(ArrayList<Cart>::new);
        return cartPage.stream().map(productMapper::cartToCartDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartDto addToCart(String userId, CartDto cartDTO) {

        //check quanity product
        Product product = productRepository.findById(cartDTO.getProduct().getId()).orElseThrow(BadRequestException::new);
        if (cartDTO.getQuantity() > product.getQuantity()) {
            throw new ProductIsNotEnoughException();
        }
        try {
            //add to cart
            Cart newCart = productMapper.cartDtoToCart(cartDTO);
            newCart.setUserId(userId);
            newCart.setPrice(product.getPrice());
            newCart.setCreated_at(LocalDateTime.now());
            newCart = cartRepository.save(newCart);
            cartDTO = productMapper.cartToCartDto(newCart);
            return cartDTO;
        } catch (Exception e) {
            throw new DataPersitException("Data persit exception " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public CartDto removeFromCart(String userId, Long cartId) {
        //remove from cart
        Cart removeCart = cartRepository.findById(cartId).orElseThrow(BadRequestException::new);
        cartRepository.delete(removeCart);
        return productMapper.cartToCartDto(removeCart);
    }
}
