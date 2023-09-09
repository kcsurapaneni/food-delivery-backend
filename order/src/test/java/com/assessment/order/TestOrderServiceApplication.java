package com.assessment.order;

import org.springframework.boot.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.testcontainers.service.connection.*;
import org.springframework.context.annotation.*;
import org.testcontainers.containers.*;
import org.testcontainers.utility.*;

@TestConfiguration(proxyBeanMethods = false)
public class TestOrderServiceApplication {

    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
    }

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:8.0.23"));
    }

    public static void main(String[] args) {
        SpringApplication.from(OrderServiceApplication::main).with(TestOrderServiceApplication.class).run(args);
    }

}
