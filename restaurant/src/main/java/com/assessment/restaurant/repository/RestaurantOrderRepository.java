package com.assessment.restaurant.repository;

import com.assessment.restaurant.model.*;
import org.springframework.data.jpa.repository.*;

/**
 * @author Krishna Chaitanya
 */
public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder, Integer> {
}
