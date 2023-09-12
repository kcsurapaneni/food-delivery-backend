package com.assessment.restaurant.service;

import com.assessment.restaurant.dto.*;
import com.assessment.restaurant.event.*;
import lombok.*;
import org.springframework.stereotype.*;

/**
 * @author Krishna Chaitanya
 */
@Service
@RequiredArgsConstructor
public class RestaurantEventService {

    private final RestaurantStatusDetailsKafkaProducer restaurantStatusDetailsKafkaProducer;

    public void sendRestaurantStatusDetailsEvent(RestaurantStatusDetails restaurantStatusDetails) {
        restaurantStatusDetailsKafkaProducer.writeToKafka(restaurantStatusDetails);
    }

}
