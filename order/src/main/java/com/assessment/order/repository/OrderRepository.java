package com.assessment.order.repository;

import com.assessment.order.dto.*;
import com.assessment.order.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

/**
 * @author Krishna Chaitanya
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Modifying
    @Query("UPDATE Order o SET o.orderStatus = :orderStatus WHERE o.id = :id")
    int updateOrderStatusById(@Param("id") Integer id, @Param("orderStatus") OrderStatus orderStatus);

}
