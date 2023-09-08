package com.assessment.order.event;

import com.assessment.order.dto.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.*;
import org.springframework.stereotype.*;

/**
 * @author Krishna Chaitanya
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderDetailsKafkaProducer {

    private final KafkaTemplate<String, OrderDetails> orderDetailsKafkaTemplate;

    @Value("${spring.kafka.order.details.topic.name:order-details}")
    private String topic;


//    @Value("${spring.kafka.replication.factor:1}")
//    private int replicationFactor;
//
//    @Value("${spring.kafka.partition.number:1}")
//    private int partitionNumber;

    public void writeToKafka(OrderDetails orderDetails) {
        orderDetailsKafkaTemplate
                .send(topic, String.valueOf(orderDetails.orderId()), orderDetails);
    }

    /**
     * as we are creating the topic using docker compose we can comment out the following snippet
     */

    /**
     @Bean
     @Order(-1) public NewTopic createNewTopic() {
     return new NewTopic(topic, partitionNumber, (short) replicationFactor);
     }
     **/

}
