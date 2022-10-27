package com.icommerce.service.impl;


import com.icommerce.dto.ProductDto;
import com.icommerce.entity.Product;
import com.icommerce.exception.BadRequestException;
import com.icommerce.exception.DataPersitException;
import com.icommerce.mapper.ProductMapper;
import com.icommerce.repository.ProductRepository;
import com.icommerce.service.ProductService;
import com.icommerce.specification.SearchCriteria;
import com.icommerce.specification.SearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @Override
    public ProductDto getProduct(@NotNull Long id) {
        return productMapper.toDto(productRepository.findById(id).orElseThrow(BadRequestException::new));
    }

    @Override
    public Page<ProductDto> getProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        return productPage.map(productMapper::toDto);
    }

    @Override
    @Transactional
    public ProductDto createProduct(@NotNull ProductDto productDto) {
        try {
            Product newProduct = productMapper.toEntity(productDto);
            newProduct.setId(null);
            newProduct.setCreated_at(LocalDateTime.now());
            newProduct = productRepository.save(newProduct);
            productDto = productMapper.toDto(newProduct);

            return productDto;
        } catch (Exception e) {
            throw new DataPersitException("Data persit exception " + e.getMessage());
        }

    }

    @Override
    @Transactional
    public ProductDto updateProduct(@NotNull ProductDto productDto, Long id) {
        Product product = productRepository.findById(id).orElseThrow(BadRequestException::new);
        try {
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setQuantity(productDto.getQuantity());
            product.setPrice(productDto.getPrice());
            product.setColor(productDto.getColor());
            product.setCategory(productMapper.categoryDtoToCategory(productDto.getCategory()));
            product.setBrand(productMapper.brandDtoToBrand(productDto.getBrand()));
            product.setUpdated_at(LocalDateTime.now());
            product = productRepository.saveAndFlush(product);
            return productMapper.toDto(product);
        } catch (Exception e) {
            throw new DataPersitException("Data persit exception " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProductDto deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Entity Not Found", "id :" + id));
        productRepository.delete(product);
        ProductDto productDto = productMapper.toDto(product);
        return productDto;

    }

    @Override
    public Page<ProductDto> searchProducts(List<SearchCriteria> criteriaList, Pageable pageable) {
        Page<Product> products = productRepository.findAll(SearchUtil.createSpec(criteriaList), pageable);
        return products.map(productMapper::toDto);
    }

}
