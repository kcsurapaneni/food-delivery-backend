package com.assessment.restaurant.event;

import com.assessment.restaurant.dto.*;
import com.assessment.restaurant.model.*;
import com.assessment.restaurant.repository.*;
import com.assessment.restaurant.service.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.*;

import java.util.concurrent.*;

/**
 * @author Krishna Chaitanya
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderDetailsEventHandler {

    private final RestaurantOrderRepository restaurantOrderRepository;
    private final RestaurantOrderItemRepository restaurantOrderItemRepository;

    private final RestaurantEventService restaurantEventService;

    @KafkaListener(topics = "${spring.kafka.order.details.topic.name:order-details}", groupId = "order-details")
    public void handle(OrderDetails orderDetails) throws InterruptedException {
        log.info("received order details event : {}", orderDetails);
        var restaurantOrder = restaurantOrderRepository.save(
                RestaurantOrder
                        .builder()
                        .orderId(orderDetails.orderId())
                        .restaurantId(orderDetails.restaurantId())
                        .billingAmount(orderDetails.billingAmount())
                        .restaurantStatus(RestaurantStatus.PROCESSING)
                        .build()
        );

        var restaurantOrderItems = orderDetails
                .items()
                .stream()
                .map(i -> RestaurantOrderItem.from(i, restaurantOrder.getId()))
                .toList();
        restaurantOrderItemRepository.saveAll(restaurantOrderItems);

        // sleep 5 seconds
        TimeUnit.SECONDS.sleep(5);

        restaurantOrder.setRestaurantStatus(RestaurantStatus.APPROVED);
        restaurantOrderRepository.save(restaurantOrder);
        restaurantEventService.sendRestaurantStatusDetailsEvent(new RestaurantStatusDetails(restaurantOrder.getOrderId(), RestaurantStatus.APPROVED.name()));
    }
}
