package com.novianto.challange6.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.novianto.challange6.util.AbstractDate;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.*;

@Entity
@Data
@Table(name = "merchants")
@Where(clause = "deleted_date is null")
public class Merchant extends AbstractDate implements Serializable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String merchantName;

    private String merchantLocation;

    private boolean open;

    @JsonIgnore
    @OneToMany(mappedBy = "merchant", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Product> products;

}
