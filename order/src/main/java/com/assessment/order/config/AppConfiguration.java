package com.assessment.order.config;

import com.assessment.order.dto.*;
import org.springframework.boot.web.client.*;
import org.springframework.context.annotation.*;
import org.springframework.kafka.config.*;
import org.springframework.kafka.core.*;
import org.springframework.web.client.*;

/**
 * @author Krishna Chaitanya
 */
@Configuration
public class AppConfiguration {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RestaurantStatusDetails> kafkaListenerContainerFactory(
            ConsumerFactory<String, RestaurantStatusDetails> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, RestaurantStatusDetails> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.getContainerProperties().setObservationEnabled(true);
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

}
