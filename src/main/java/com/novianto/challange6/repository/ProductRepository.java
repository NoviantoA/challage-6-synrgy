package com.novianto.challange6.repository;

import com.novianto.challange6.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query(value = "SELECT p FROM Product p WHERE p.id = :id")
    public Product getByIdProduct(@Param("id") UUID id);

    @Query(value = "SELECT p FROM product p WHERE id = :id", nativeQuery = true)
    public Object getByIdNative(@Param("id") UUID id);

    @Query(value = "select p from Product p where LOWER(p.productName) like LOWER(:nameParam)")
    public Page<Product> getByLikeName(@Param("nameParam") String nameParam, Pageable pageable);

    @Query(value = "select p from Product p ")
    public Page<Product> getALlPage(Pageable pageable);

    Page<Product> findByProductNameAndPrice(String productName, Double price, Pageable pageable);

    @Query("select count(p) from Product p where p.productName = ?1")
    long countByName(String name);

    boolean existsByProductName(String productName);

    long count();

    @Query("select sum(p.id) from Product p")
    long sumProduct();

    @Modifying
    @Procedure(name = "get_all_products")
    List<Product> getAllProducts();

    @Modifying
    @Procedure(name = "get_product_by_id")
    Product getProductById(@Param("p_product_id") UUID productId);

    @Modifying
    @Procedure(name = "insert_product")
    void insertProduct(@Param("p_product_name") String productName, @Param("p_price") Double price, @Param("p_merchant_id") UUID merchantId);

    @Modifying
    @Procedure(name = "update_product")
    void updateProduct(@Param("p_product_id") UUID productId, @Param("p_product_name") String productName, @Param("p_price") Double price, @Param("p_merchant_id") UUID merchantId);

    @Modifying
    @Procedure(name = "delete_product")
    void deleteProduct(@Param("p_product_id") UUID productId);
}
