package com.novianto.challange6.service;

import com.novianto.challange6.dto.OrderDto;
import com.novianto.challange6.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface OrderService {

    Page<Order> getAllOrder(Pageable pageable);

    Map<String, Object> saveOrder(OrderDto orderDto);

    Map<String, Object> updateOrder(UUID idOrder, OrderDto orderDto);

    Map<String, Object> deleteOrder(UUID idOrder);

    Map<String, Object> getOrderById(UUID idOrder);

    Page<Order> getCompletedOrders(Pageable pageable);
}
