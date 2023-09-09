package com.assessment.order.repository;

import com.assessment.order.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.test.context.*;
import org.testcontainers.junit.jupiter.*;

import java.math.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
class ItemsRepositoryTest {

    @Autowired
    ItemsRepository itemsRepository;

    @Test
    void testFindPriceById() {
        // When
        Optional<Item> priceById = itemsRepository.findPriceById(1);

        // Then
        assertThat(priceById).isPresent();
        assertThat(priceById).isPresent().map(Item::getId).hasValue(1);
        assertThat(priceById).isPresent().map(Item::getPrice).hasValue(BigDecimal.valueOf(14.99));
        assertThat(priceById).isPresent().map(Item::getName).hasValue("Chicken Tikka Masala");

    }
}