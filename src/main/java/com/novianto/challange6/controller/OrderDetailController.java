package com.novianto.challange6.controller;

import com.novianto.challange6.dto.OrderDetailDto;
import com.novianto.challange6.entity.OrderDetail;
import com.novianto.challange6.repository.OrderDetailRepository;
import com.novianto.challange6.service.OrderDetailService;
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
@RequestMapping("/v1/order-detail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private SimpleStringUtil simpleStringUtil;
    @Autowired
    public Response response;

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> saveOrderDetail(@RequestBody OrderDetailDto request) {
        try {
            return new ResponseEntity<Map>(orderDetailService.saveOrderDetail(request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/update/{orderDetailId}", "/update/{orderDetailId}/"})
    public ResponseEntity<Map> updateOrderDetail(@RequestBody OrderDetailDto request, @PathVariable("orderDetailId") UUID orderDetailId) {
        try {
            if (orderDetailId == null) {
                return new ResponseEntity<Map>(response.routeNotFound(ConfigValidation.ID_ORDER_DETAIL_REQUIRED), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Map>(orderDetailService.updateOrderDetail(orderDetailId, request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = {"/delete/{orderDetailId}", "/delete/{orderDetailId}/"})
    public ResponseEntity<Map> deleteOrderDetail(@PathVariable("orderDetailId") UUID orderDetailId) {
        try {
            return new ResponseEntity<Map>(orderDetailService.deleteOrderDetail(orderDetailId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/get/{orderDetailId}", "/get/{orderDetailId}/"})
    public ResponseEntity<Map> getOrderDetailById(@PathVariable("orderDetailId") UUID orderDetailId) {
        try {
            return new ResponseEntity<Map>(response.successResponse(orderDetailRepository.getByIdOrderDetail(orderDetailId)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all-order-details")
    public Page<OrderDetail> getAllOrderDetail(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderDetailService.getAllOrderDetail(pageable);
    }

    @GetMapping("/user/{userId}")
    public Page<OrderDetail> getAllOrderDetailByIdUser(@PathVariable UUID userId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderDetailService.getOrderDetailByIdUser(userId, pageable);
    }

    @GetMapping(value = {"/list-spec", "/list-spec/"})
    public ResponseEntity<Map> listOrderDetailHeaderSpec(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) Double totalPrice,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable show_data = simpleStringUtil.getShort(orderby, ordertype, page, size);
        Specification<OrderDetail> spec =
                ((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (totalPrice != null && !totalPrice.isNaN()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + totalPrice + "%"));
                    }
                    if (quantity != null) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("price")), "%" + quantity + "%"));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });
        Page<OrderDetail> list = orderDetailRepository.findAll(spec, show_data);
        return new ResponseEntity<Map>(response.successResponse(list), new HttpHeaders(), HttpStatus.OK);
    }
}
