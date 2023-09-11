package com.assessment.order.service;

import com.assessment.order.dto.*;
import com.assessment.order.exception.customer.*;
import com.assessment.order.exception.item.*;
import com.assessment.order.model.*;
import com.assessment.order.repository.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * @author Krishna Chaitanya
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderVerificationService {

    private final ItemsRepository itemsRepository;
    private final CustomerRepository customerRepository;

    public boolean verifyOrder(final OrderRequest orderRequest) {
        int customerId = orderRequest.customerId();
        log.debug("customer id : {}", customerId);
        var customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            throw new CustomerNotFoundException(customerId);
        }
        var itemIdList = orderRequest.items().stream().map(Items::itemId).toList();
        List<Item> itemList = itemsRepository.findAllById(itemIdList);
        if (itemList.size() != itemIdList.size()) {
            throw new ItemNotFoundException(itemList.stream().map(Item::getId).toList(), itemIdList);
        }
        return true;
    }

}
