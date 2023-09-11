package com.assessment.order.event;

import com.assessment.order.dto.*;
import com.assessment.order.repository.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

/**
 * @author Krishna Chaitanya
 */
@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class RestaurantStatusEventHandler {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "${spring.kafka.restaurant.status.topic.name:restaurant-status}", groupId = "restaurant-status")
    public void handleRestaurantStatus(RestaurantStatusDetails restaurantStatusDetails) {
        log.info("Restaurant Status Details : {}", restaurantStatusDetails);
        int recordsUpdated = orderRepository.updateOrderStatusById(
                restaurantStatusDetails.orderId(),
                OrderStatus.valueOf(restaurantStatusDetails.status())
        );
        log.info("Records updated : {}", recordsUpdated);
    }

}
