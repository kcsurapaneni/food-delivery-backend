package com.assessment.order.event;

import com.assessment.order.dto.*;
import com.assessment.order.model.Order;
import com.assessment.order.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.testcontainers.service.connection.*;
import org.springframework.kafka.core.*;
import org.springframework.test.context.*;
import org.testcontainers.containers.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.*;
import org.testcontainers.shaded.org.awaitility.*;
import org.testcontainers.utility.*;

import java.math.*;
import java.time.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class RestaurantStatusEventHandlerTest {

    @Container
    @ServiceConnection
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    );

    @Value("${spring.kafka.restaurant.status.topic.name}")
    private String restaurantStatusTopicName;

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private KafkaTemplate<String, RestaurantStatusDetails> restaurantStatusDetailsKafkaTemplate;

    @BeforeAll
    static void beforeAll() {
        kafka.start();
    }

    @AfterAll
    static void afterAll() {
        kafka.stop();
    }

    @BeforeEach
    void setUp() {
        orderRepository.save(
                new Order(1, 2, "14 Avenue, SW, Calgary", OrderStatus.PROCESSING, BigDecimal.TEN)
        );
    }

    @Test
    void testHandleRestaurantStatus() {

        // Given
        RestaurantStatusDetails restaurantStatusDetails = new RestaurantStatusDetails(1, OrderStatus.APPROVED.name());

        // When
        restaurantStatusDetailsKafkaTemplate.send(restaurantStatusTopicName, String.valueOf(restaurantStatusDetails.orderId()), restaurantStatusDetails);

        // Then
        Awaitility
                .await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> {
                            Optional<Order> optionalOrder = orderRepository.findById(1);
                            assertThat(optionalOrder).isPresent();
                            assertThat(optionalOrder.get().getOrderStatus()).isEqualTo(OrderStatus.APPROVED);
                        }
                );
    }

}