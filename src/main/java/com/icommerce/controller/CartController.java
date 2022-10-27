package com.icommerce.controller;

import com.icommerce.dto.CartDto;
import com.icommerce.dto.ProductDto;
import com.icommerce.exception.NotPermissionException;
import com.icommerce.service.CartService;
import com.icommerce.service.ProductService;
import com.icommerce.specification.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {
  private final Logger log = LoggerFactory.getLogger(CartController.class);
  @Autowired
  CartService cartService;

  @GetMapping(value = "/{userId}")
  public ResponseEntity<List<CartDto>> getCartByUser(@PathVariable("userId") String userId) {
    log.info("CartController#getCartByUser --- userId: {}", userId);
    List<CartDto> dtoPage = cartService.getCartsByUser(userId);
    return ResponseEntity.ok(dtoPage);
  }

  @GetMapping(value = "")
  public ResponseEntity<Page<CartDto>> getCarts(Pageable pageable) {
    log.info("CartController#getCarts {}", pageable);
    Page<CartDto> dtoPage = cartService.getCarts(pageable);
    return ResponseEntity.ok(dtoPage);
  }

  @PostMapping("/{userId}/addToCart")
  public ResponseEntity<CartDto> addToCart(@PathVariable("userId") String userId,
                                              @RequestBody @NotNull @Valid CartDto cartDto) {
    log.info("CartController#addToCart userId: {}", userId);
    //check permission of userId
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    String username = ((UserDetails) authentication.getPrincipal()).getUsername();
    if(!userId.equals(username)){
      throw new NotPermissionException("User has not permission");
    }
    CartDto dto = cartService.addToCart(userId,cartDto);
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("/{userId}/removeFromCart/{cartId}")
  public ResponseEntity<CartDto> removeFromCart(@PathVariable("userId") String userId,
                                                @PathVariable("cartId") Long cartId) {
    log.info("CartController#removeFromCart userId: {}", userId);
    //check permission of user
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    String username = ((UserDetails) authentication.getPrincipal()).getUsername();
    if(!userId.equals(username)){
      throw new NotPermissionException("User has not permission");
    }
    CartDto dto = cartService.removeFromCart(userId,cartId);
    return ResponseEntity.ok(dto);
  }
}
