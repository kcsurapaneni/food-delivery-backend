package com.assessment.order.controller;

import com.assessment.order.repository.*;
import io.restassured.*;
import io.restassured.http.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.server.*;
import org.springframework.boot.testcontainers.service.connection.*;
import org.springframework.test.context.*;
import org.testcontainers.containers.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    ).withEnv("KAFKA_CREATE_TOPICS", "order-details");

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

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
        RestAssured.baseURI = "http://localhost:" + port + "/api";
    }

    @Test
    void testRequestOrder() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "restaurant_id" : 1,
                          "customer_id": 1,
                          "delivery_address": "14 Avenue, SW, Calgary",
                          "items" : [
                            {
                              "item_id": 1,
                              "quantity": 2,
                              "notes": "NOTE-1"
                            },
                            {
                              "item_id": 3,
                              "quantity": 4,
                              "notes": "NOTE-2"
                            }
                          ]
                        }
                        """)
                .when()
                .post("/order")
                .then()
                .statusCode(201)
                .body(
                        "order_id", is(1),
                        "order_status", equalTo("PROCESSING"),
                        "billing_amount", equalTo(81.94F)
                );

        assertThat(orderRepository.findAll()).isNotEmpty();
        assertThat(orderRepository.findAll()).hasSize(1);

        assertThat(orderItemRepository.findAll()).isNotEmpty();
        assertThat(orderItemRepository.findAll()).hasSize(2);
    }


}