package com.novianto.challange6.dto;

import com.novianto.challange6.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDto {
    private Date orderTime;
    private String destinationAddress;
    private boolean completed;
    private User user;
}
