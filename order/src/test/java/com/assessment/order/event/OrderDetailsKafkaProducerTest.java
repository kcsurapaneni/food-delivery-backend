package com.assessment.order.event;

import com.assessment.order.dto.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.testcontainers.service.connection.*;
import org.springframework.kafka.support.serializer.*;
import org.springframework.test.context.*;
import org.testcontainers.containers.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.*;
import org.testcontainers.shaded.com.google.common.collect.*;
import org.testcontainers.shaded.org.awaitility.*;
import org.testcontainers.utility.*;

import java.math.*;
import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class OrderDetailsKafkaProducerTest {

    @Container
    @ServiceConnection
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    ).withEnv("KAFKA_CREATE_TOPICS", "order-details");

    @Autowired
    private OrderDetailsKafkaProducer orderDetailsKafkaProducer;

    static private KafkaConsumer<String, OrderDetails> kafkaConsumer;

    @BeforeAll
    static void beforeAll() {
        JsonDeserializer<OrderDetails> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");
        kafkaConsumer = new KafkaConsumer<>(
                ImmutableMap.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers(),
                        ConsumerConfig.GROUP_ID_CONFIG, "order-details-consumer",
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                        "allow.auto.create.topics", "false"
                ),
                new StringDeserializer(),
                deserializer
        );
        kafkaConsumer.subscribe(Collections.singletonList("order-details"));
    }

    @AfterAll
    static void afterAll() {
        kafka.stop();
    }

    @Test
    void orderDetailsShouldWriteToKafka() {
        // Given
        var orderDetails = new OrderDetails(1, 2, 3, "14 Avenue, SW, Calgary",
                BigDecimal.TEN,
                List.of(
                        new Items(4, 5, "NOTE-1"),
                        new Items(6, 7, "NOTE-2")
                )
        );

        // When
        orderDetailsKafkaProducer.writeToKafka(orderDetails);

        // Then
        Awaitility
                .await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(Duration.ofSeconds(20))
                .untilAsserted(
                        () -> {
                            var orderDetailsConsumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
                            assertThat(orderDetailsConsumerRecords).isNotEmpty();
                            for (ConsumerRecord<String, OrderDetails> order : orderDetailsConsumerRecords) {
                                OrderDetails value = order.value();
                                assertThat(value.orderId()).isEqualTo(1);
                                assertThat(value.customerId()).isEqualTo(2);
                                assertThat(value.restaurantId()).isEqualTo(3);
                                assertThat(value.deliveryAddress()).isEqualTo("14 Avenue, SW, Calgary");
                                assertThat(value.billingAmount()).isEqualTo(BigDecimal.TEN);
                                assertThat(value.items()).isNotEmpty();
                                assertThat(value.items()).hasSize(2);
                                // although order is not static, just checking
                                assertThat(value.items().get(0).itemId()).isEqualTo(4);
                                assertThat(value.items().get(0).quantity()).isEqualTo(5);
                                assertThat(value.items().get(0).notes()).isEqualTo("NOTE-1");
                                assertThat(value.items().get(1).itemId()).isEqualTo(6);
                                assertThat(value.items().get(1).quantity()).isEqualTo(7);
                                assertThat(value.items().get(1).notes()).isEqualTo("NOTE-2");
                            }
                        }
                );
    }

}