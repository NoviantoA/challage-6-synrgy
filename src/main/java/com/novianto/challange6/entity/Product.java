package com.novianto.challange6.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.novianto.challange6.util.AbstractDate;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "products")
@Where(clause = "deleted_date is null")
public class Product extends AbstractDate implements Serializable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String productName;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "merchantId", referencedColumnName = "id")
    private Merchant merchant;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    public void setMerchant(UUID merchantId) {
        Merchant merchant = new Merchant();
        merchant.setId(merchantId);
        this.merchant = merchant;
    }
}
