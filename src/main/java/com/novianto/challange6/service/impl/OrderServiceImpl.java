package com.novianto.challange6.service.impl;

import com.novianto.challange6.dto.OrderDto;
import com.novianto.challange6.entity.Order;
import com.novianto.challange6.entity.User;
import com.novianto.challange6.repository.OrderRepository;
import com.novianto.challange6.repository.UserRepository;
import com.novianto.challange6.service.OrderService;
import com.novianto.challange6.util.ConfigValidation;
import com.novianto.challange6.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Response response;

    @Override
    public Page<Order> getAllOrder(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Map<String, Object> saveOrder(OrderDto orderDto) {
        Map<String, Object> responseMap = new HashMap<>();

        if (orderDto == null) {
            return response.errorResponse(ConfigValidation.ORDER_DATA_INVALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        } else {
            if (orderDto.getOrderTime() == null) {
                return response.errorResponse(ConfigValidation.ORDER_TIME_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            } else if (!response.isValidDate(orderDto.getOrderTime())) {
                return response.errorResponse(ConfigValidation.ORDER_TIME_NOT_VALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            }
        }

        try {
            if (orderDto.getDestinationAddress() == null || orderDto.getDestinationAddress().trim().isEmpty()) {
                return response.errorResponse(ConfigValidation.DESTINATION_ADDRESS_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            }

            if (orderDto.getUser() == null || orderDto.getUser().getId() == null || orderDto.getUser().getId().toString().trim().isEmpty()) {
                return response.errorResponse(ConfigValidation.USER_REQUIRED, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            }

            UUID userId = UUID.fromString(orderDto.getUser().getId().toString().trim());
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                Order order = new Order();
                order.setId(UUID.randomUUID());
                order.setOrderTime(orderDto.getOrderTime());
                order.setDestinationAddress(orderDto.getDestinationAddress());
                order.setCompleted(false);
                order.setUser(user.getId());

                Order savedOrder = orderRepository.save(order);
                responseMap = response.successResponse(savedOrder);
            } else {
                return response.errorResponse(ConfigValidation.ID_USER_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return response.errorResponse(ConfigValidation.USER_ID_INVALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        } catch (DataAccessException e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }

        return responseMap;
    }

    @Override
    public Map<String, Object> updateOrder(UUID idOrder, OrderDto orderDto) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            Optional<Order> existingOrder = Optional.ofNullable(orderRepository.getByIdOrder(idOrder));

            if (existingOrder.isPresent()) {
                Order updatedOrder = existingOrder.get();

                // Validasi request body
                if (orderDto.getOrderTime() != null && !response.isValidDate(orderDto.getOrderTime())) {
                    return response.errorResponse(ConfigValidation.ORDER_TIME_NOT_VALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
                }

                if (orderDto.getDestinationAddress() != null && orderDto.getDestinationAddress().trim().isEmpty()) {
                    return response.errorResponse(ConfigValidation.DESTINATION_ADDRESS_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
                }

                // Update order properties
                if (orderDto.getOrderTime() != null) {
                    updatedOrder.setOrderTime(orderDto.getOrderTime());
                }
                if (orderDto.getDestinationAddress() != null) {
                    updatedOrder.setDestinationAddress(orderDto.getDestinationAddress());
                }
                updatedOrder.setCompleted(orderDto.isCompleted());

                orderRepository.save(updatedOrder);
                responseMap = response.successResponse(updatedOrder);
            } else {
                responseMap = response.errorResponse(ConfigValidation.ID_ORDER_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
        } catch (Exception e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }

        return responseMap;
    }

    @Override
    public Map<String, Object> deleteOrder(UUID idOrder) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            Optional<Order> orderToDelete = Optional.ofNullable(orderRepository.getByIdOrder(idOrder));

            if (orderToDelete.isPresent()) {
                Order order = orderToDelete.get();
                order.setDeleted_date(new Date());
                orderRepository.save(order);

                responseMap = response.successResponse(ConfigValidation.SUCCESS);
            } else {
                responseMap = response.errorResponse(ConfigValidation.ID_ORDER_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
        } catch (Exception e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }
        return responseMap;
    }

    @Override
    public Map<String, Object> getOrderById(UUID idOrder) {
        Map<String, Object> responseMap = new HashMap<>();

        Optional<Order> orderOptional = Optional.ofNullable(orderRepository.getByIdOrder(idOrder));

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            responseMap = response.successResponse(order);
        } else {
            responseMap = response.errorResponse(ConfigValidation.ID_ORDER_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
        }
        return responseMap;
    }

    @Override
    public Page<Order> getCompletedOrders(Pageable pageable) {
        return orderRepository.findCompletedOrders(pageable);
    }
}
