package com.assessment.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.*;

/**
 * @author Krishna Chaitanya
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @Column(name = "item_id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

}
