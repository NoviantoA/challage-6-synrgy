package com.novianto.challange6.service.impl;

import com.novianto.challange6.dto.ProductDto;
import com.novianto.challange6.entity.Merchant;
import com.novianto.challange6.entity.Product;
import com.novianto.challange6.repository.MerchantRepository;
import com.novianto.challange6.repository.ProductRepository;
import com.novianto.challange6.service.ProductService;
import com.novianto.challange6.util.ConfigValidation;
import com.novianto.challange6.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Response response;
    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public Page<Product> getAllProduct(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Map<String, Object> saveProduct(ProductDto productDto) {
        Map<String, Object> responseMap = new HashMap<>();

        if (productDto == null) {
            return response.errorResponse(ConfigValidation.PRODUCT_DATA_INVALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        } else {
            if (productDto.getProductName() == null || productDto.getProductName().trim().isEmpty()) {
                return response.errorResponse(ConfigValidation.PRODUCT_NAME_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            } else if (!response.isValidName(productDto.getProductName())) {
                return response.errorResponse(ConfigValidation.PRODUCT_NAME_NOT_VALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            } else if (productDto.getPrice() == null) {
                return response.errorResponse(ConfigValidation.PRICE_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            } else if (!response.isValidPrice(productDto.getPrice())) {
                return response.errorResponse(ConfigValidation.PRICE_NOT_VALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            } else if (productDto.getMerchant() == null || productDto.getMerchant().getId() == null || productDto.getMerchant().getId().toString().trim().isEmpty()) {
                return response.errorResponse(ConfigValidation.MERCHANT_ID_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            }
        }

        try {
            if (productRepository.existsByProductName(productDto.getProductName())) {
                return response.errorResponse(ConfigValidation.PRODUCT_NAME_ALREADY_EXISTS, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            }

            UUID merchantId = UUID.fromString(productDto.getMerchant().getId().toString().trim());
            Optional<Merchant> merchantOptional = Optional.ofNullable(merchantRepository.getByIdMerchant(merchantId));

            if (merchantOptional.isPresent()) {
                Product product = new Product();
                product.setId(UUID.randomUUID());
                product.setProductName(productDto.getProductName());
                product.setPrice(productDto.getPrice());

                Merchant merchant = merchantOptional.get();
                product.setMerchant(merchant.getId());

                Product savedProduct = productRepository.save(product);
                responseMap = response.successResponse(savedProduct);
            } else {
                return response.errorResponse(ConfigValidation.ID_MERCHANT_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return response.errorResponse(ConfigValidation.MERCHANT_ID_INVALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        } catch (DataAccessException e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }
        return responseMap;
    }


    @Override
    public Map<String, Object> updateProduct(UUID idProduct, ProductDto productDto) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            Optional<Product> existingProduct = productRepository.findById(idProduct);

            if (existingProduct.isPresent()) {
                Product updatedProduct = existingProduct.get();

                if (productDto.getProductName() != null && productDto.getProductName().trim().isEmpty()) {
                    return response.errorResponse(ConfigValidation.PRODUCT_NAME_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
                }
                if (productDto.getPrice() != null && !response.isValidPrice(productDto.getPrice())) {
                    return response.errorResponse(ConfigValidation.PRICE_NOT_VALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
                }
                if (productDto.getMerchant() == null || productDto.getMerchant().getId() == null || productDto.getMerchant().getId().toString().trim().isEmpty()) {
                    return response.errorResponse(ConfigValidation.MERCHANT_ID_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
                }

                // Check if merchantId exists
                UUID merchantId = UUID.fromString(productDto.getMerchant().getId().toString().trim());
                Optional<Merchant> merchantOptional = merchantRepository.findById(merchantId);

                if (!merchantOptional.isPresent()) {
                    return response.errorResponse(ConfigValidation.ID_MERCHANT_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
                }

                // Update the product
                if (productDto.getProductName() != null) {
                    updatedProduct.setProductName(productDto.getProductName());
                }
                if (productDto.getPrice() != null) {
                    updatedProduct.setPrice(productDto.getPrice());
                }

                productRepository.save(updatedProduct);
                responseMap = response.successResponse(updatedProduct);
            } else {
                responseMap = response.errorResponse(ConfigValidation.ID_PRODUCT_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            // Handle invalid UUID format
            return response.errorResponse(ConfigValidation.MERCHANT_ID_INVALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        } catch (Exception e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }
        return responseMap;
    }


    @Override
    public Map<String, Object> deleteProduct(UUID idProduct) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            Optional<Product> productToDelete = Optional.ofNullable(productRepository.getByIdProduct(idProduct));

            if (productToDelete.isPresent()) {
                Product product = productToDelete.get();
                product.setDeleted_date(new Date());
                productRepository.save(product);

                responseMap = response.successResponse(ConfigValidation.SUCCESS);
            } else {
                responseMap = response.errorResponse(ConfigValidation.ID_PRODUCT_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
        } catch (Exception e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }
        return responseMap;
    }

    @Override
    public Map<String, Object> getProductById(UUID idProduct) {
        Map<String, Object> responseMap = new HashMap<>();

        Optional<Product> productOptional = Optional.ofNullable(productRepository.getProductById(idProduct));

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            responseMap = response.successResponse(product);
        } else {
            responseMap = response.errorResponse(ConfigValidation.ID_PRODUCT_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
        }
        return responseMap;
    }
}
