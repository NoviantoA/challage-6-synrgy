package com.novianto.challange6.service.impl;

import com.novianto.challange6.dto.OrderDetailDto;
import com.novianto.challange6.entity.*;
import com.novianto.challange6.repository.OrderDetailRepository;
import com.novianto.challange6.repository.OrderRepository;
import com.novianto.challange6.repository.ProductRepository;
import com.novianto.challange6.service.OrderDetailService;
import com.novianto.challange6.util.ConfigValidation;
import com.novianto.challange6.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private Response response;

    @Override
    public Page<OrderDetail> getAllOrderDetail(Pageable pageable) {
        return orderDetailRepository.findAll(pageable);
    }

    @Override
    public Map<String, Object> saveOrderDetail(OrderDetailDto orderDetailDto) {
        Map<String, Object> responseMap = new HashMap<>();

        if (orderDetailDto == null) {
            return response.errorResponse(ConfigValidation.ORDER_DETAIL_DATA_INVALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        } else {
            if (orderDetailDto.getQuantity() == null || !response.isValidQuantity(orderDetailDto.getQuantity())) {
                return response.errorResponse(ConfigValidation.QUANTITY_NOT_VALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            }
        }

        try {
            if (orderDetailDto.getProduct() == null || orderDetailDto.getProduct().getId() == null || orderDetailDto.getProduct().getId().toString().trim().isEmpty()) {
                return response.errorResponse(ConfigValidation.PRODUCT_REQUIRED, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            }
            if (orderDetailDto.getOrder() == null || orderDetailDto.getOrder().getId() == null || orderDetailDto.getOrder().getId().toString().trim().isEmpty()) {
                return response.errorResponse(ConfigValidation.ORDER_REQUIRED, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            }

            UUID orderId = UUID.fromString(orderDetailDto.getOrder().getId().toString().trim());
            UUID productId = UUID.fromString(orderDetailDto.getProduct().getId().toString().trim());

            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            Optional<Product> optionalProduct = productRepository.findById(productId);

            if (!optionalOrder.isPresent()) {
                return response.errorResponse(ConfigValidation.ID_ORDER_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
            if (!optionalProduct.isPresent()) {
                return response.errorResponse(ConfigValidation.ID_PRODUCT_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(UUID.randomUUID());
            orderDetail.setQuantity(orderDetailDto.getQuantity());

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                Double totalPrice = product.getPrice() * orderDetailDto.getQuantity();
                orderDetail.setTotalPrice(totalPrice);
            }

            Order order = optionalOrder.get();
            orderDetail.setOrder(order);

            Product product = optionalProduct.get();
            orderDetail.setProduct(product);

            OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
            responseMap = response.successResponse(savedOrderDetail);

        } catch (IllegalArgumentException e) {
            return response.errorResponse(ConfigValidation.USER_ID_INVALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        } catch (DataAccessException e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }
        return responseMap;
    }


    @Override
    public Map<String, Object> updateOrderDetail(UUID idOrderDetail, OrderDetailDto orderDetailDto) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            Optional<OrderDetail> existingOrderDetail = Optional.ofNullable(orderDetailRepository.getByIdOrderDetail(idOrderDetail));

            if (existingOrderDetail.isPresent()) {
                OrderDetail updatedOrderDetail = existingOrderDetail.get();

                if (orderDetailDto.getQuantity() != null && !response.isValidQuantity(orderDetailDto.getQuantity())) {
                    return response.errorResponse(ConfigValidation.QUANTITY_NOT_VALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
                }

                if (orderDetailDto.getTotalPrice() != null && !response.isValidPrice(orderDetailDto.getTotalPrice())) {
                    return response.errorResponse(ConfigValidation.PRICE_NOT_VALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
                }

                 if (orderDetailDto.getQuantity() != null) {
                    updatedOrderDetail.setQuantity(orderDetailDto.getQuantity());
                }
                if (orderDetailDto.getTotalPrice() != null) {
                    updatedOrderDetail.setTotalPrice(orderDetailDto.getTotalPrice());
                }

                orderDetailRepository.save(updatedOrderDetail);
                responseMap = response.successResponse(updatedOrderDetail);
            } else {
                responseMap = response.errorResponse(ConfigValidation.ID_ORDER_DETAIL_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
        } catch (Exception e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }

        return responseMap;
    }

    @Override
    public Map<String, Object> deleteOrderDetail(UUID idOrderDetail) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            Optional<OrderDetail> orderDetailToDelete = Optional.ofNullable(orderDetailRepository.getOrderDetailById(idOrderDetail));

            if (orderDetailToDelete.isPresent()) {
                OrderDetail orderDetail = orderDetailToDelete.get();
                orderDetail.setDeleted_date(new Date());
                orderDetailRepository.save(orderDetail);

                responseMap = response.successResponse(ConfigValidation.SUCCESS);
            } else {
                responseMap = response.errorResponse(ConfigValidation.ID_ORDER_DETAIL_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
        } catch (Exception e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }
        return responseMap;
    }

    @Override
    public Map<String, Object> getOrderDetailById(UUID idOrderDetail) {
        Map<String, Object> responseMap = new HashMap<>();

        Optional<OrderDetail> orderDetailOptional = Optional.ofNullable(orderDetailRepository.getOrderDetailById(idOrderDetail));

        if (orderDetailOptional.isPresent()) {
            OrderDetail orderDetail = orderDetailOptional.get();
            responseMap = response.successResponse(orderDetail);
        } else {
            responseMap = response.errorResponse(ConfigValidation.ID_ORDER_DETAIL_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
        }
        return responseMap;
    }

    @Override
    public Page<OrderDetail> getOrderDetailByIdUser(UUID userId, Pageable pageable) {
        return orderDetailRepository.findOrderDetailByUserId(userId, pageable);
    }
}
