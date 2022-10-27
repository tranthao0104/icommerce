package com.icommerce.service;


import com.icommerce.dto.CartDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.List;


public interface CartService {

    List<CartDto> getCartsByUser(@NotNull String userId);

    Page<CartDto> getCarts(Pageable pageable);

    CartDto addToCart(@NotNull String userId, @NotNull CartDto cartDTO);

    CartDto removeFromCart(@NotNull String userId,@NotNull Long cartId);

}
