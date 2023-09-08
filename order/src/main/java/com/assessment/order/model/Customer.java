package com.assessment.order.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Krishna Chaitanya
 */
@Setter
@Getter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 15)
    private String phone;

}
