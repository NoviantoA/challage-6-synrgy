package com.novianto.challange6.dto;

import com.novianto.challange6.entity.Order;
import com.novianto.challange6.entity.Product;
import lombok.Data;

@Data
public class OrderDetailDto {
    private Integer quantity;
    private Double totalPrice;
    private Order order;
    private Product product;
}
