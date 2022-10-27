package com.icommerce.controller;

import com.icommerce.dto.ProductDto;
import com.icommerce.service.ProductService;
import com.icommerce.specification.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
  private final Logger log = LoggerFactory.getLogger(ProductController.class);
  @Autowired
  ProductService productService;

  @GetMapping(value = "{id}")
  public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id) {
    log.info("ProductController#getProduct --- productId: {}", id);
    ProductDto productDto = productService.getProduct(id);
    return ResponseEntity.ok(productDto);
  }

  @GetMapping(value = "")
  public ResponseEntity<Page<ProductDto>> getProducts(Pageable pageable) {
    log.info("ProductController#getProducts {}", pageable);
    Page<ProductDto> dtoPage = productService.getProducts(pageable);
    return ResponseEntity.ok(dtoPage);
  }

  @PostMapping(value = "search")
  public ResponseEntity<Page<ProductDto>> searchProducts(@RequestBody List<SearchCriteria> criteriaList, Pageable pageable){
    log.info("ProductController#searchProducts");
    Page<ProductDto> dtoPage = productService.searchProducts(criteriaList, pageable);
    return ResponseEntity.ok(dtoPage);
  }

  @PostMapping(value = "")
  public ResponseEntity<ProductDto> createProduct(@RequestBody @NotNull @Valid ProductDto productDto) {
    log.info("ProductController#createProduct --- productName: {}", productDto.getName());
    ProductDto dto = productService.createProduct(productDto);
    return ResponseEntity.ok(dto);
  }

  @PutMapping(value = "{id}")
  public ResponseEntity<ProductDto> updateProduct(@RequestBody @NotNull @Valid ProductDto productDto ,
                                                  @PathVariable("id") @NotEmpty Long id) {
    log.info("ProductController#updateProduct --- productId: {}", id);
    ProductDto dto = productService.updateProduct(productDto, id);
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    log.info("ProductController#delete --- productId: {}", id);
    ProductDto product = productService.deleteProduct(id);
    return new ResponseEntity<>(product, HttpStatus.OK);
  }

}
