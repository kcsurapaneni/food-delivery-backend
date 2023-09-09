package com.assessment.order;

import com.assessment.order.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceApplicationTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @Test
    void contextLoads() {
        assertThat(customerRepository).isNotNull();
        assertThat(itemsRepository).isNotNull();

        assertThat(customerRepository.findAll()).isNotEmpty().hasSize(3);
        assertThat(itemsRepository.findAll()).isNotEmpty().hasSize(5);
    }

}
