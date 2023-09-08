package com.assessment.order.repository;

import com.assessment.order.model.*;
import org.springframework.data.jpa.repository.*;

/**
 * @author Krishna Chaitanya
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
