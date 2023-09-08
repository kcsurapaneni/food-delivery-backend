package com.assessment.order.repository;

import com.assessment.order.model.*;
import org.springframework.data.jpa.repository.*;

/**
 * @author Krishna Chaitanya
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
