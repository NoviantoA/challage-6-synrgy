package com.novianto.challange6.service;

import com.novianto.challange6.dto.ProductDto;
import com.novianto.challange6.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface ProductService {
    Page<Product> getAllProduct(Pageable pageable);

    Map<String, Object> saveProduct(ProductDto productDto);

    Map<String, Object> updateProduct(UUID idProduct, ProductDto productDto);

    Map<String, Object> deleteProduct(UUID idProduct);

    Map<String, Object> getProductById(UUID idProduct);
}
