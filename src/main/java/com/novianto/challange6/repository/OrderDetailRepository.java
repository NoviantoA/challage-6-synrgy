package com.novianto.challange6.repository;

import com.novianto.challange6.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID>, JpaSpecificationExecutor<OrderDetail> {

    @Query(value = "SELECT od FROM OrderDetail od WHERE od.id = :id")
    public OrderDetail getByIdOrderDetail(@Param("id") UUID id);

    @Query(value = "SELECT od FROM order_details od WHERE id = :id", nativeQuery = true)
    public Object getByIdNative(@Param("id") UUID id);

    @Query(value = "select od from OrderDetail od where od.totalPrice like :nameParam")
    public Page<OrderDetail> getByLikeTotalPrice(@Param("nameParam") Double nameParam, Pageable pageable);

    @Query(value = "select od from OrderDetail od ")
    public Page<OrderDetail> getALlPage(Pageable pageable);

    @Query(value = "SELECT od FROM OrderDetail od WHERE od.quantity = :quantity AND od.totalPrice = :totalPrice", nativeQuery = false)
    public OrderDetail findByOrderDetailQuantityAndTotalPriceWithQuery(@Param("quantity") Integer quantity, @Param("totalPrice") Double totalPrice);

    Page<OrderDetail> findByQuantityAndTotalPrice(Integer quantity, Double totalPrice, Pageable pageable);

    @Query("select count(od) from OrderDetail od where od.totalPrice = ?1")
    long countByTotalPrice(Double totalPrice);

    @Query("SELECT od FROM OrderDetail od JOIN od.order o JOIN o.user u WHERE u.id = :userId")
    Page<OrderDetail> findOrderDetailByUserId(@Param("userId") UUID userId, Pageable pageable);

    long count();

    @Query("select sum(od.id) from OrderDetail od")
    long sumProduct();

    @Modifying
    @Procedure(name = "get_all_order_detail")
    List<OrderDetail> getAllOrderDetail();

    @Modifying
    @Procedure(name = "get_order_detail_by_id")
    OrderDetail getOrderDetailById(@Param("p_order_detail_id") UUID orderDetailId);

    @Modifying
    @Procedure(name = "insert_order_detail")
    void insertOrderDetail(
            @Param("p_order_time") Date orderTime,
            @Param("p_destination_address") String destinationAddress,
            @Param("p_completed") boolean completed,
            @Param("p_user_id") UUID userId
    );

    @Modifying
    @Procedure(name = "update_order_detail")
    void updateOrderDetail(
            @Param("p_order_detail_id") UUID orderDetailId,
            @Param("p_order_time") Date orderTime,
            @Param("p_destination_address") String destinationAddress,
            @Param("p_completed") boolean completed
    );

    @Modifying
    @Procedure(name = "delete_order_detail")
    void deleteOrderDetail(@Param("p_order_detail_id") UUID orderDetailId);
}
