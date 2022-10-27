package com.icommerce.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.icommerce.ICommerceApplication;
import com.icommerce.dto.CartDto;
import com.icommerce.mapper.ProductMapper;
import com.icommerce.mapper.ProductMapperImpl;
import com.icommerce.security.JWTAuthenticationManager;
import com.icommerce.service.CartService;
import com.icommerce.utils.MockObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@ContextConfiguration(classes = { ICommerceApplication.class, JWTAuthenticationManager.class})
class CartControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    private ProductMapper productMapper = new ProductMapperImpl();

    private ObjectMapper mapper = new ObjectMapper();

    private static final String AUTHORIZATION = "Authorization";

    @Test
    void getCartsByUserId() throws Exception {
        String url = "/carts/test-user";
        List<CartDto> cartDtos = MockObject.mockCarts().stream().
                map(c ->productMapper.cartToCartDto(c)).collect(Collectors.toList());
        String respStr = "[{\"id\":1,\"quantity\":100,\"price\":50.5,\"userId\":\"test-user\",\"product\":{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\"}},{\"id\":2,\"quantity\":200,\"price\":100.5,\"userId\":\"test-user\",\"product\":{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\"}}]";
        // When
        when(cartService.getCartsByUser(MockObject.TEST_USER)).thenReturn(cartDtos);

        String resp = this.mockMvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, MockObject.BEARER_TOKEN))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void getCarts() throws Exception {
        String url = "/carts?page=0&size=10";
        String respStr = "{\"content\":[{\"id\":1,\"quantity\":100,\"price\":50.5,\"userId\":\"test-user\",\"product\":{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\"}},{\"id\":2,\"quantity\":200,\"price\":100.5,\"userId\":\"test-user\",\"product\":{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\"}}],\"pageable\":\"INSTANCE\",\"totalElements\":2,\"totalPages\":1,\"last\":true,\"size\":2,\"number\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"first\":true,\"numberOfElements\":2,\"empty\":false}";
        Pageable pageable = PageRequest.of(0, 10);
        List<CartDto> cartDtos = MockObject.mockCarts()
                .stream()
                .map(productMapper::cartToCartDto)
                .collect(Collectors.toList());
        when(cartService.getCarts(pageable)).thenReturn(new PageImpl<>(cartDtos));
        String resp = this.mockMvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, MockObject.BEARER_TOKEN))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr.length(), resp.length());
    }


    @Test
    void addToCart() throws Exception {
        String url = "/carts/test-user/addToCart";
        CartDto cartDto = productMapper.cartToCartDto(MockObject.mockCarts().get(0));
        String content = mapper.writeValueAsString(cartDto);
        String respStr = "{\"id\":1,\"quantity\":100,\"price\":50.5,\"userId\":\"test-user\",\"product\":{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\"}}";
        when(cartService.addToCart(MockObject.TEST_USER,cartDto)).thenReturn(cartDto);
        String resp = this.mockMvc
                .perform(post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, MockObject.BEARER_TOKEN))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void addToCart_not_permission() throws Exception {
        String url = "/carts/test-user1/addToCart";
        CartDto cartDto = productMapper.cartToCartDto(MockObject.mockCarts().get(0));
        String content = mapper.writeValueAsString(cartDto);
        String respStr = "{\"statusCode\":\"FORBIDDEN\",\"message\":\"User has not permission\",\"data\":null}";
        when(cartService.addToCart("test-user1",cartDto)).thenReturn(cartDto);
        String resp = this.mockMvc
                .perform(post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, MockObject.BEARER_TOKEN))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void removeFromCart() throws Exception {
        String url = "/carts/test-user/removeFromCart/10";
        CartDto cartDto = productMapper.cartToCartDto(MockObject.mockCarts().get(0));

        String respStr = "{\"id\":1,\"quantity\":100,\"price\":50.5,\"userId\":\"test-user\",\"product\":{\"id\":1,\"name\":\"product 1\",\"quantity\":100,\"price\":40.5,\"color\":\"Black\"}}";
        when(cartService.removeFromCart(MockObject.TEST_USER, 10L)).thenReturn(cartDto);
        String resp = this.mockMvc
                .perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, MockObject.BEARER_TOKEN))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void removeFromCart_not_permission() throws Exception {
        String url = "/carts/test-user1/removeFromCart/10";
        CartDto cartDto = productMapper.cartToCartDto(MockObject.mockCarts().get(0));

        String respStr = "{\"statusCode\":\"FORBIDDEN\",\"message\":\"User has not permission\",\"data\":null}";
        when(cartService.removeFromCart("test-user1", 10L)).thenReturn(cartDto);
        String resp = this.mockMvc
                .perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, MockObject.BEARER_TOKEN))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }
}