package com.icommerce.service.impl;


import com.icommerce.dto.ProductDto;
import com.icommerce.entity.Product;
import com.icommerce.exception.BadRequestException;
import com.icommerce.exception.DataPersitException;
import com.icommerce.mapper.ProductMapper;
import com.icommerce.repository.ProductRepository;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTests {
    @Mock
    ProductRepository productRepository;

    @Spy
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @InjectMocks
    ProductServiceImpl productService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(MockObject.mockProducts()));
        Page<ProductDto> productDtoPage = productService.getProducts(pageable);
        Assertions.assertEquals(3, productDtoPage.getSize());
    }

    @Test
    void getProduct() {
        Product prod = MockObject.mockProducts().get(0);
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(prod));
        ProductDto productDto = productService.getProduct(1L);
        Assertions.assertEquals(1L, productDto.getId());
        Assertions.assertEquals("product 1", productDto.getName());
    }

    @Test
    void getProduct_NotFound() {
        Assertions.assertThrows(BadRequestException.class,() -> productService.getProduct(10L));
    }

    @Test
    void searchProducts() {
        when(productRepository.findAll(nullable(Specification.class), nullable(Pageable.class)))
                .thenReturn(new PageImpl<>(MockObject.mockProducts()));
        Page<ProductDto> productDtoPage = productService.searchProducts(new ArrayList<>(), PageRequest.of(0, 10));
        Assertions.assertEquals(3, productDtoPage.getSize());
    }

    @Test
    void createProduct() {
        ProductDto productDto = ProductDto.builder()
                .name("new product")
                .quantity(200L)
                .price(50f)
                .build();
        Product product = new Product();
        product.setId(10L);
        product.setName("new product");
        product.setQuantity(200L);
        product.setPrice(50f);
        product.setCreated_at(LocalDateTime.now());
        when(productRepository.save(any(Product.class))).thenReturn(product);
        productDto = productService.createProduct(productDto);
        Assertions.assertEquals(10L, productDto.getId());
        Assertions.assertEquals("new product", productDto.getName());
    }

    @Test
    void createProduct_PersitDB_Exception() {
        ProductDto productDto = ProductDto.builder()
                .name("new product")
                .quantity(200L)
                .price(50f)
                .build();
        Product product = new Product();
        product.setId(10L);
        product.setName("new product");
        product.setQuantity(200L);
        product.setPrice(50f);
        product.setCreated_at(LocalDateTime.now());
        when(productRepository.save(any(Product.class))).thenThrow(new DataPersitException());
        Assertions.assertThrows(DataPersitException.class, () -> productService.createProduct(productDto));

    }

    @Test
    void updateProduct() {
        ProductDto productDto = ProductDto.builder()
                .name("new product")
                .quantity(200L)
                .price(50f)
                .build();
        Product product = new Product();
        product.setId(10L);
        product.setName("new product");
        product.setQuantity(200L);
        product.setPrice(50f);
        product.setUpdated_at(LocalDateTime.now());
        when(productRepository.findById(10L)).thenReturn(Optional.ofNullable(product));
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);
        productDto = productService.updateProduct(productDto, 10L);
        Assertions.assertEquals(10L, productDto.getId());
        Assertions.assertEquals("new product", productDto.getName());
    }

    @Test
    void updateProduct_PersitDb_Exception() {
        ProductDto productDto = ProductDto.builder()
                .name("new product")
                .quantity(200L)
                .price(50f)
                .build();
        Product product = new Product();
        product.setId(10L);
        product.setName("new product");
        product.setQuantity(200L);
        product.setPrice(50f);
        product.setUpdated_at(LocalDateTime.now());
        when(productRepository.findById(10L)).thenReturn(Optional.ofNullable(product));
        when(productRepository.saveAndFlush(any(Product.class))).thenThrow(new DataPersitException());
        Assertions.assertThrows(DataPersitException.class,() ->productService.updateProduct(productDto, 10L) );
    }

    @Test
    void delete() {
        Product product = new Product();
        product.setId(10L);
        product.setName("new product");
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        ProductDto productDto = productService.deleteProduct(10L);
        Assertions.assertEquals(10L, productDto.getId());
        Assertions.assertEquals("new product", productDto.getName());
    }

    @Test
    void delete_NotFound() {
        Assertions.assertThrows(BadRequestException.class,()-> productService.deleteProduct(10L));
    }
}