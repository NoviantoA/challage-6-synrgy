package com.novianto.challange6.controller;

import com.novianto.challange6.dto.OrderDto;
import com.novianto.challange6.entity.Order;
import com.novianto.challange6.repository.OrderRepository;
import com.novianto.challange6.service.OrderService;
import com.novianto.challange6.util.ConfigValidation;
import com.novianto.challange6.util.Response;
import com.novianto.challange6.util.SimpleStringUtil;
import javax.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SimpleStringUtil simpleStringUtil;
    @Autowired
    public Response response;

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> saveOrder(@RequestBody OrderDto request) {
        try {
            return new ResponseEntity<Map>(orderService.saveOrder(request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/update/{orderId}", "/update/{orderId}/"})
    public ResponseEntity<Map> updateOrder(@RequestBody OrderDto request, @PathVariable("orderId") UUID orderId) {
        try {
            if (orderId == null) {
                return new ResponseEntity<Map>(response.routeNotFound(ConfigValidation.ID_ORDER_REQUIRED), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Map>(orderService.updateOrder(orderId, request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = {"/delete/{orderId}", "/delete/{orderId}/"})
    public ResponseEntity<Map> deleteOrder(@PathVariable("orderId") UUID orderId) {
        try {
            return new ResponseEntity<Map>(orderService.deleteOrder(orderId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/get/{orderId}", "/get/{orderId}/"})
    public ResponseEntity<Map> getOrderById(@PathVariable("orderId") UUID orderId) {
        try {
            return new ResponseEntity<Map>(response.successResponse(orderRepository.getByIdOrder(orderId)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all-orders")
    public Page<Order> getAllOrder(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderService.getAllOrder(pageable);
    }

    @GetMapping("/completed")
    public Page<Order> getAllOrderCompleted(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findCompletedOrders(pageable);
    }

    @GetMapping(value = {"/list-spec", "/list-spec/"})
    public ResponseEntity<Map> listOrderHeaderSpec(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String orderTime,
            @RequestParam(required = false) String destinationAddress,
            @RequestParam(required = false) Boolean isCompleted,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable show_data = simpleStringUtil.getShort(orderby, ordertype, page, size);
        Specification<Order> spec =
                ((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (orderTime != null && !orderTime.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("orderTime")), "%" + orderTime.toLowerCase() + "%"));
                    }
                    if (destinationAddress != null && !destinationAddress.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("destinationAddress")), "%" + destinationAddress.toLowerCase() + "%"));
                    }
                    if (isCompleted != null) {
                        predicates.add(criteriaBuilder.equal(root.get("isCompleted"), isCompleted));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });
        Page<Order> list = orderRepository.findAll(spec, show_data);
        return new ResponseEntity<Map>(response.successResponse(list), new HttpHeaders(), HttpStatus.OK);
    }
}
