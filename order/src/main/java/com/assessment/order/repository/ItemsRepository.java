package com.assessment.order.repository;

import com.assessment.order.model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

/**
 * @author Krishna Chaitanya
 */
public interface ItemsRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findPriceById(Integer id);

}
