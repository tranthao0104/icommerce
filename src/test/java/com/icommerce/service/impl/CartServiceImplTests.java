package com.icommerce.service.impl;


import com.icommerce.dto.CartDto;
import com.icommerce.dto.ProductDto;
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
import com.icommerce.utils.MockObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTests {
    @Mock
    CartRepository cartRepository;

    @Mock
    ProductRepository productRepository;

    @Spy
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @InjectMocks
    CartServiceImpl cartService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCarts() {
        Pageable pageable = PageRequest.of(0, 10);
        when(cartRepository.findAll(pageable)).thenReturn(new PageImpl<>(MockObject.mockCarts()));
        Page<CartDto> cartDtoPage = cartService.getCarts(pageable);
        Assertions.assertEquals(2, cartDtoPage.getSize());
    }

    @Test
    void getCartsByUser() {
        when(cartRepository.findByUserId(MockObject.TEST_USER)).thenReturn(Optional.ofNullable(MockObject.mockCarts()));
        List<CartDto> carts = cartService.getCartsByUser(MockObject.TEST_USER);
        Assertions.assertEquals(2, carts.size());
    }

    @Test
    void getCartsByUser_NoResults() {
        List<CartDto> carts = cartService.getCartsByUser(MockObject.TEST_USER);
        Assertions.assertEquals(0, carts.size());
    }

    @Test
    void addToCart() {
        Product prod = MockObject.mockProducts().get(0);
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(prod));
        CartDto cartDto = CartDto.builder()
                .userId(MockObject.TEST_USER)
                .quantity(100l)
                .product(productMapper.toDto(prod))
                .build();
        Cart cart = new Cart();
        cart.setId(10L);
        cart.setQuantity(100l);
        cart.setUserId(MockObject.TEST_USER);
        cart.setProduct(prod);
        cart.setCreated_at(LocalDateTime.now());
        cart.setUpdated_at(LocalDateTime.now());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        cartDto = cartService.addToCart(MockObject.TEST_USER, cartDto);
        Assertions.assertEquals(10L, cartDto.getId());
    }

    @Test
    void addToCart_product_is_not_enough() {
        Product prod = MockObject.mockProducts().get(0);
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(prod));
        CartDto cartDto = CartDto.builder()
                .userId(MockObject.TEST_USER)
                .quantity(200l)
                .product(productMapper.toDto(prod))
                .build();
        Cart cart = new Cart();
        cart.setId(10L);
        cart.setQuantity(200l);
        cart.setUserId(MockObject.TEST_USER);
        cart.setProduct(prod);
        Assertions.assertThrows(ProductIsNotEnoughException.class,
                ()-> cartService.addToCart(MockObject.TEST_USER, cartDto));
    }

    @Test
    void addToCart_persitDB_exception() {
        Product prod = MockObject.mockProducts().get(0);
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(prod));
        CartDto cartDto = CartDto.builder()
                .userId(MockObject.TEST_USER)
                .quantity(100l)
                .product(productMapper.toDto(prod))
                .build();
        Cart cart = new Cart();
        cart.setId(10L);
        cart.setQuantity(100l);
        cart.setUserId(MockObject.TEST_USER);
        cart.setProduct(prod);
        cart.setCreated_at(LocalDateTime.now());
        cart.setUpdated_at(LocalDateTime.now());
        when(cartRepository.save(any(Cart.class))).thenThrow(new DataPersitException());
        Assertions.assertThrows(DataPersitException.class,()->cartService.addToCart(MockObject.TEST_USER, cartDto));
    }

    @Test
    void removeFromCart() {
        Product prod = MockObject.mockProducts().get(0);
        Cart cart = new Cart();
        cart.setId(10l);
        cart.setQuantity(100l);
        cart.setUserId(MockObject.TEST_USER);
        cart.setProduct(prod);
        when(cartRepository.findById(10l)).thenReturn(Optional.of(cart));
        CartDto cartDto = cartService.removeFromCart(MockObject.TEST_USER, 10l);
        Assertions.assertEquals(10L, cartDto.getId());
    }

}