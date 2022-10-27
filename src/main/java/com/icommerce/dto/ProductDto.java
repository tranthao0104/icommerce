package com.icommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
//    @NotNull
    private Long id;

    @Length(max = 255)
    @NotEmpty
    private String name;

    private String description;

    @Min(value = 0, message = "min quantity is 0")
    @Max(value = 1000000, message = "max quantity is 1000000")
    @NotNull
    private Long quantity;

    @Min(value = 0, message = "min price is 0")
    @NotNull
    private Float price;

    @NotEmpty
    private  String color;

    @NotNull
    private  CategoryDto category;

    @NotNull
    private  BrandDto brand;

}
