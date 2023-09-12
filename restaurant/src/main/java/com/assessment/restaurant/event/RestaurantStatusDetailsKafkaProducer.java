package com.assessment.restaurant.event;

import com.assessment.restaurant.dto.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.kafka.core.*;
import org.springframework.stereotype.*;

/**
 * @author Krishna Chaitanya
 */
@Component
@Slf4j
public class RestaurantStatusDetailsKafkaProducer {

    private final KafkaTemplate<String, RestaurantStatusDetails> orderDetailsKafkaTemplate;

    @Value("${spring.kafka.restaurant.status.topic.name:restaurant-status}")
    private String topic;

    public RestaurantStatusDetailsKafkaProducer(KafkaTemplate<String, RestaurantStatusDetails> orderDetailsKafkaTemplate) {
        this.orderDetailsKafkaTemplate = orderDetailsKafkaTemplate;
        this.orderDetailsKafkaTemplate.setObservationEnabled(true);
    }

    public void writeToKafka(RestaurantStatusDetails restaurantStatusDetails) {
        orderDetailsKafkaTemplate
                .send(topic, String.valueOf(restaurantStatusDetails.orderId()), restaurantStatusDetails);
    }


}
