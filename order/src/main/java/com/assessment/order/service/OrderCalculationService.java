package com.assessment.order.service;

import com.assessment.order.dto.*;
import com.assessment.order.exception.item.*;
import com.assessment.order.model.*;
import com.assessment.order.repository.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

/**
 * @author Krishna Chaitanya
 */
@Service
@RequiredArgsConstructor
public class OrderCalculationService {

    private final ItemsRepository itemsRepository;

    public BigDecimal calculateBill(List<Items> items) {
        BigDecimal bill = BigDecimal.ZERO;
        for (Items item : items) {
            Optional<Item> itemOptional = itemsRepository.findPriceById(item.itemId());
            if (itemOptional.isEmpty()) {
                throw new ItemNotFoundException(item.itemId());
            }
            bill = bill.add(itemOptional.get().getPrice().multiply(BigDecimal.valueOf(item.quantity())));
        }
        return bill;
    }

}
