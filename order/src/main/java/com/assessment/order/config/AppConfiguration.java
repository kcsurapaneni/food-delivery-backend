package com.assessment.order.config;

import com.assessment.order.dto.*;
import com.fasterxml.jackson.databind.*;
import io.swagger.v3.core.jackson.*;
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

//    @Bean
//    RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }

    @Bean
    RestClient restClient(RestClient.Builder builder) {
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

    /**
     * This bean has been added so that in the swagger-ui, we will see snake_case attributes instead of camelCase <br/>
     * Swagger UI Path :- /api/swagger-ui/index.html <br/>
     * <a href="https://github.com/springdoc/springdoc-openapi/issues/66#issuecomment-560335774">Reference link</a>
     */
    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

}
