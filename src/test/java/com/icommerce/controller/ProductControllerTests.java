package com.icommerce.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.icommerce.ICommerceApplication;
import com.icommerce.dto.ProductDto;
import com.icommerce.mapper.ProductMapper;
import com.icommerce.mapper.ProductMapperImpl;
import com.icommerce.security.JWTAuthenticationManager;
import com.icommerce.service.ProductService;
import com.icommerce.utils.MockObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = { ICommerceApplication.class, JWTAuthenticationManager.class})
class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductMapper productMapper = new ProductMapperImpl();

    private ObjectMapper mapper = new ObjectMapper();

    private static final String AUTHORIZATION = "Authorization";

    @Test
    void getProduct() throws Exception {
        String url = "/products/10";
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .quantity(20L)
                .price(10000f)
                .build();
        String respStr = "{\"id\":1,\"quantity\":20,\"price\":10000.0}";
        // When
        when(productService.getProduct(10L)).thenReturn(productDto);

        String resp = this.mockMvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_token"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void getProducts() throws Exception {
        String url = "/products?page=0&size=10";
        String respStr = "{\"content\":[{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\",\"category\":{\"id\":1,\"name\":\"Category 1\"},\"brand\":{\"id\":1,\"name\":\"Brand 1\"}},{\"id\":2,\"name\":\"product 2\",\"quantity\":200,\"price\":50.0,\"color\":\"Black\",\"category\":{\"id\":1,\"name\":\"Category 1\"},\"brand\":{\"id\":2,\"name\":\"Brand 2\"}},{\"id\":3,\"name\":\"product 3\",\"quantity\":150,\"price\":30.0,\"color\":\"White\",\"category\":{\"id\":2,\"name\":\"Category 2\"},\"brand\":{\"id\":2,\"name\":\"Brand 2\"}}],\"pageable\":\"INSTANCE\",\"totalElements\":3,\"totalPages\":1,\"last\":true,\"size\":3,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"numberOfElements\":3,\"first\":true,\"empty\":false}";
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductDto> products = MockObject.mockProducts()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        when(productService.getProducts(pageable)).thenReturn(new PageImpl<>(products));
        String resp = this.mockMvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_token"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr.length(), resp.length());
    }

    @Test
    void searchProducts() throws Exception {
        String url = "/products/search?page=0&size=10";
        String respStr = "{\"content\":[{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\",\"category\":{\"id\":1,\"name\":\"Category 1\"},\"brand\":{\"id\":1,\"name\":\"Brand 1\"}},{\"id\":2,\"name\":\"product 2\",\"quantity\":200,\"price\":50.0,\"color\":\"Black\",\"category\":{\"id\":1,\"name\":\"Category 1\"},\"brand\":{\"id\":2,\"name\":\"Brand 2\"}},{\"id\":3,\"name\":\"product 3\",\"quantity\":150,\"price\":30.0,\"color\":\"White\",\"category\":{\"id\":2,\"name\":\"Category 2\"},\"brand\":{\"id\":2,\"name\":\"Brand 2\"}}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":3,\"size\":3,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":3,\"empty\":false}";
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductDto> products = MockObject.mockProducts()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        when(productService.searchProducts(new ArrayList<>(), pageable)).thenReturn(new PageImpl<>(products));
        String resp = this.mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]")
                        .header(AUTHORIZATION, "Bearer mock_token"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr.length(), resp.length());
    }

    @Test
    void createProduct() throws Exception {
        String url = "/products";
        ProductDto productDto = productMapper.toDto(MockObject.mockProducts().get(0));
        String content = mapper.writeValueAsString(productDto);
        String respStr = "{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\",\"category\":{\"id\":1,\"name\":\"Category 1\"},\"brand\":{\"id\":1,\"name\":\"Brand 1\"}}";
        when(productService.createProduct(productDto)).thenReturn(productDto);
        String resp = this.mockMvc
                .perform(post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_token"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void updateProduct() throws Exception {
        String url = "/products/1";
        ProductDto productDto = productMapper.toDto(MockObject.mockProducts().get(0));
        String content = mapper.writeValueAsString(productDto);
        String respStr = "{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\",\"category\":{\"id\":1,\"name\":\"Category 1\"},\"brand\":{\"id\":1,\"name\":\"Brand 1\"}}";
        when(productService.updateProduct(productDto, 1L)).thenReturn(productDto);
        String resp = this.mockMvc
                .perform(put(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_token"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void deleteProduct() throws Exception {
        String url = "/products/1";
        ProductDto productDto = productMapper.toDto(MockObject.mockProducts().get(0));
        String respStr = "{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\",\"category\":{\"id\":1,\"name\":\"Category 1\"},\"brand\":{\"id\":1,\"name\":\"Brand 1\"}}";
        when(productService.deleteProduct(1L)).thenReturn(productDto);
        String resp = this.mockMvc
                .perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_token"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }
}