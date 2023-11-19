package com.novianto.challange6.service;

import com.novianto.challange6.dto.OrderDetailDto;
import com.novianto.challange6.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface OrderDetailService {
    Page<OrderDetail> getAllOrderDetail(Pageable pageable);

    Map<String, Object> saveOrderDetail(OrderDetailDto orderDetailDto);

    Map<String, Object> updateOrderDetail(UUID idOrderDetail, OrderDetailDto orderDetailDto);

    Map<String, Object> deleteOrderDetail(UUID idOrderDetail);

    Map<String, Object> getOrderDetailById(UUID idOrderDetail);

    Page<OrderDetail> getOrderDetailByIdUser(UUID userId, Pageable pageable);
}
