package com.novianto.challange6.repository;

import com.novianto.challange6.entity.Order;
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
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    @Query(value = "SELECT o FROM Order o WHERE o.id = :id")
    public Order getByIdOrder(@Param("id") UUID id);

    @Query(value = "SELECT o FROM orders o WHERE id = :id", nativeQuery = true)
    public Object getByIdNative(@Param("id") UUID id);

    @Query(value = "select o from Order o where o.destinationAddress like :nameParam")
    public Page<Order> getByLikeDestinationAddress(@Param("nameParam") String nameParam, Pageable pageable);

    @Query(value = "select o from Order o")
    public Page<Order> getALlPage(Pageable pageable);

    @Query(value = "SELECT o FROM Order o WHERE o.orderTime = :orderTime AND o.destinationAddress = :destinationAddress", nativeQuery = false)
    public Order findByOrderTimeAndDestinationAddressWithQuery(@Param("orderTime") Date orderTime, @Param("destinationAddress") String destinationAddress);

    Page<Order> findByOrderTimeAndDestinationAddress(Date orderTime, String destinationAddress, Pageable pageable);

    @Query("select count(o) from Order o where o.destinationAddress = ?1")
    long countByDestinationAddress(String destinationAddress);

    @Query("SELECT o FROM Order o WHERE o.completed = true")
    Page<Order> findCompletedOrders(Pageable pageable);

    long count();

    @Query("select sum(o.id) from Order o")
    long sumMerchant();

    @Modifying
    @Procedure(name = "get_all_order")
    List<Order> getAllOrder();

    @Modifying
    @Procedure(name = "get_order_by_id")
    Order getOrderById(@Param("p_order_id") UUID orderId);

    @Modifying
    @Procedure(name = "insert_order")
    void insertOrder(
            @Param("p_order_time") Date orderTime,
            @Param("p_destination_address") String destinationAddress,
            @Param("p_completed") boolean completed,
            @Param("p_user_id") UUID userId
    );

    @Modifying
    @Procedure(name = "update_order")
    void updateOrder(
            @Param("p_order_id") UUID orderId,
            @Param("p_order_time") Date orderTime,
            @Param("p_destination_address") String destinationAddress,
            @Param("p_completed") boolean completed
    );

    @Procedure(name = "delete_order")
    void deleteOrder(@Param("p_order_id") UUID orderId);
}