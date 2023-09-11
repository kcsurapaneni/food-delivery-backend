package com.assessment.order.model;

import com.assessment.order.dto.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.*;
import java.time.*;
import java.util.*;

/**
 * @author Krishna Chaitanya
 */
@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_id")
//    private Customer customer;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "billing_amount")
    private BigDecimal billingAmount;

    public static Optional<OrderResponse> from(Order order) {
        if (Objects.isNull(order)) {
            return Optional.empty();
        }
        return Optional.of(new OrderResponse(order.getId(), order.getOrderStatus(), order.getBillingAmount(), Instant.now()));
    }

}
