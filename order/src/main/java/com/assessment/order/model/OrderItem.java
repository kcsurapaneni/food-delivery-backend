package com.assessment.order.model;

import com.assessment.order.dto.*;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Krishna Chaitanya
 */
@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(nullable = false)
    private Integer quantity;

    private String notes;

    public static OrderItem from(Items item, Integer orderId) {
        return OrderItem
                .builder()
                .orderId(orderId)
                .itemId(item.itemId())
                .quantity(item.quantity())
                .notes(item.notes())
                .build();
    }

}
