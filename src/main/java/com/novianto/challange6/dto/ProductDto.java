package com.novianto.challange6.dto;

import com.novianto.challange6.entity.Merchant;
import lombok.Data;

@Data
public class ProductDto {
    private String productName;
    private Double price;
    private Merchant merchant;
}
