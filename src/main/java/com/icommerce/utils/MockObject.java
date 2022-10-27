package com.icommerce.utils;


import com.icommerce.entity.Brand;
import com.icommerce.entity.Cart;
import com.icommerce.entity.Category;
import com.icommerce.entity.Product;

import java.util.List;

public class MockObject {

    public static final String TEST_USER = "test-user";
    public static final String BEARER_TOKEN = "Bearer mock_token";
    public static List<Product> mockProducts() {
        // Category
        Category category_1 = new Category(1l, "Category 1", false);
        Category category_2 = new Category(2l, "Category 2", false);
        // Brand
        Brand brand_1 = new Brand(1l, "Brand 1", false);
        Brand brand_2 = new Brand(2l, "Brand 2", false);
        // Product
        Product product_1 = new Product(1L, "product 1", null,
                100L, 40.5f, category_1, brand_1, "Black");
        Product product_2 = new Product(2L, "product 2", null,
                200L, 50f, category_1, brand_2, "Black");
        Product product_3 = new Product(3L, "product 3", null,
                150L, 30f, category_2, brand_2, "White");
        List<Product> products = List.of(product_1,
                product_2,
                product_3
               );

        return products;
    }

    public static List<Cart> mockCarts() {
        Product product_1 = new Product(1L, "product 1", null,
                100L, 40.5f, null, null, "Black");
        Cart cart_1 = new Cart(1L,TEST_USER,product_1,100l,50.5f);
        Cart cart_2 = new Cart(2L,TEST_USER,product_1,200l,100.5f);
        List<Cart> carts = List.of(cart_1,cart_2);
        return carts;
    }
}
