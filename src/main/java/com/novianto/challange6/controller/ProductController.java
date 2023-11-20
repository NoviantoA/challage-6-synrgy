package com.novianto.challange6.controller;

import com.novianto.challange6.dto.ProductDto;
import com.novianto.challange6.entity.Product;
import com.novianto.challange6.repository.ProductRepository;
import com.novianto.challange6.service.ProductService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SimpleStringUtil simpleStringUtil;
    @Autowired
    public Response response;

    @PostMapping(value = {"/save", "/save/"})
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Map> saveProduct(@RequestBody ProductDto request) {
        try {
            return new ResponseEntity<Map>(productService.saveProduct(request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/update/{productId}", "/update/{productId}/"})
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Map> updateProduct(@RequestBody ProductDto request, @PathVariable("productId") UUID productId) {
        try {
            if (productId == null) {
                return new ResponseEntity<Map>(response.routeNotFound(ConfigValidation.ID_PRODUCT_REQUIRED), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Map>(productService.updateProduct(productId, request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = {"/delete/{productId}", "/delete/{productId}/"})
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Map> deleteProduct(@PathVariable("productId") UUID productId) {
        try {
            return new ResponseEntity<Map>(productService.deleteProduct(productId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/get/{productId}", "/get/{productId}/"})
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MERCHANT')")
    public ResponseEntity<Map> getProductById(@PathVariable("productId") UUID productId) {
        try {
            return new ResponseEntity<Map>(response.successResponse(productRepository.getByIdProduct(productId)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all-products")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MERCHANT')")
    public Page<Product> getAllProduct(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProduct(pageable);
    }

    @GetMapping(value = {"/list-spec", "/list-spec/"})
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MERCHANT')")
    public ResponseEntity<Map> listProductHeaderSpec(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable show_data = simpleStringUtil.getShort(orderby, ordertype, page, size);
        Specification<Product> spec =
                ((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (productName != null && !productName.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + productName.toLowerCase() + "%"));
                    }
                    if (price != null && !price.isNaN()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("price")), "%" + price + "%"));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });
        Page<Product> list = productRepository.findAll(spec, show_data);
        return new ResponseEntity<Map>(response.successResponse(list), new HttpHeaders(), HttpStatus.OK);
    }
}
