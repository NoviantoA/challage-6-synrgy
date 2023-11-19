package com.novianto.challange6.entity;

import com.novianto.challange6.util.AbstractDate;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "order_details")
@Where(clause = "deleted_date is null")
public class OrderDetail extends AbstractDate implements Serializable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private Integer quantity;

    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "id")
    private Product product;
}
