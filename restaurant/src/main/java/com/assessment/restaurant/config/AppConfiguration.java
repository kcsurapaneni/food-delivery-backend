package com.assessment.restaurant.config;

import com.assessment.restaurant.dto.*;
import org.springframework.context.annotation.*;
import org.springframework.kafka.config.*;
import org.springframework.kafka.core.*;

/**
 * @author Krishna Chaitanya
 */
@Configuration
public class AppConfiguration {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RestaurantStatusDetails> kafkaListenerContainerFactory(
            ConsumerFactory<String, RestaurantStatusDetails> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, RestaurantStatusDetails> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.getContainerProperties().setObservationEnabled(true);
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

}
