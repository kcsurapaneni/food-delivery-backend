package com.assessment.order.service;

import com.assessment.order.dto.*;
import com.assessment.order.exception.customer.*;
import com.assessment.order.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.boot.testcontainers.service.connection.*;
import org.springframework.test.context.*;
import org.testcontainers.containers.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.*;

import java.math.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    @Container
    @ServiceConnection
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    ).withEnv("KAFKA_CREATE_TOPICS", "order-details");

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.23"))
            .withDatabaseName("order")
            .withInitScript("db/init-schema.sql");

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @MockBean
    private InvoiceService invoiceService;

    @BeforeAll
    static void beforeAll() {
        kafka.start();
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        kafka.stop();
        mySQLContainer.stop();
    }

    @Test
    void testRequestOrder() {
        // Given
        BDDMockito.given(invoiceService.sendInvoice(ArgumentMatchers.any())).willReturn(true);

        var orderRequest = new OrderRequest(1, 2, "14 Avenue, SW, Calgary",
                List.of(
                        new Items(3, 3, "NOTE-1"),
                        new Items(5, 5, "NOTE-2")
                )
        );

        // When
        var optionalOrderResponse = orderService.requestOrder(orderRequest);

        // Then
        assertThat(optionalOrderResponse).isPresent();
        assertThat(optionalOrderResponse).map(OrderResponse::orderId).hasValue(1);
        assertThat(optionalOrderResponse).map(OrderResponse::orderStatus).hasValue(OrderStatus.PROCESSING);
        assertThat(optionalOrderResponse).map(OrderResponse::billingAmount).hasValue(BigDecimal.valueOf(133.92));

        assertThat(orderRepository.findAll()).isNotEmpty();
        assertThat(orderRepository.findAll()).hasSize(1);

        assertThat(orderItemRepository.findAll()).isNotEmpty();
        assertThat(orderItemRepository.findAll()).hasSize(2);

    }

    @Test
    void testRequestOrderFailedWithCustomerNotFoundException() {
        // Given
        var orderRequest = new OrderRequest(1, 10, "14 Avenue, SW, Calgary",
                List.of(
                        new Items(3, 3, "NOTE-1"),
                        new Items(5, 5, "NOTE-2")
                )
        );

        // When & Then
        assertThatThrownBy(() -> orderService.requestOrder(orderRequest))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("customer with id 10 not found");

    }


}