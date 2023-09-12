package com.assessment.restaurant.model;

import com.assessment.restaurant.dto.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.*;

/**
 * @author Krishna Chaitanya
 */
@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant_order")
public class RestaurantOrder {

    @Id
    @Column(name = "restaurant_order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "restaurant_id", nullable = false)
    private Integer restaurantId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "billing_amount", nullable = false)
    private BigDecimal billingAmount;

    @Column(name = "restaurant_status", nullable = false)
    private RestaurantStatus restaurantStatus;

}
