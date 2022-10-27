package com.icommerce.mapper;


import com.icommerce.dto.BrandDto;
import com.icommerce.dto.CartDto;
import com.icommerce.dto.CategoryDto;
import com.icommerce.dto.ProductDto;
import com.icommerce.entity.Brand;
import com.icommerce.entity.Cart;
import com.icommerce.entity.Category;
import com.icommerce.entity.Product;
import org.mapstruct.Mapper;

import java.awt.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductDto dto);

    ProductDto toDto(Product entity);

    CategoryDto categoryToCategoryDto(Category entity);

    Category categoryDtoToCategory(CategoryDto dto);

    BrandDto brandToBrandDto(Brand entity);

    Brand brandDtoToBrand(BrandDto dto);

    CartDto cartToCartDto(Cart dto);

    Cart cartDtoToCart(CartDto entity);
}
