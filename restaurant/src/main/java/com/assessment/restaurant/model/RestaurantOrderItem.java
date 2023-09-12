package com.assessment.restaurant.model;

import com.assessment.restaurant.dto.*;
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
@Table(name = "restaurant_order_items")
public class RestaurantOrderItem {

    @Id
    @Column(name = "restaurant_order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "restaurant_order_id", nullable = false)
    private Integer restaurantOrderId;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "notes", nullable = false)
    private String notes;

    public static RestaurantOrderItem from(Items item, Integer restaurantOrderId) {
        return RestaurantOrderItem
                .builder()
                .restaurantOrderId(restaurantOrderId)
                .itemId(item.itemId())
                .quantity(item.quantity())
                .notes(item.notes())
                .build();
    }

}
