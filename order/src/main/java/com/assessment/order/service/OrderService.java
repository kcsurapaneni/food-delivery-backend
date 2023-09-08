package com.assessment.order.service;

import com.assessment.order.dto.*;
import com.assessment.order.exception.customer.*;
import com.assessment.order.exception.item.*;
import com.assessment.order.exception.order.*;
import com.assessment.order.model.*;
import com.assessment.order.repository.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

/**
 * @author Krishna Chaitanya
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final EventService eventService;

    private final OrderRepository orderRepository;
    private final ItemsRepository itemsRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;

    public Optional<OrderResponse> requestOrder(OrderRequest orderRequest) {
        try {
            int customerId = orderRequest.customerId();
            log.debug("customer id : {}", customerId);
            var customerOptional = customerRepository.findById(customerId);
            if (customerOptional.isEmpty()) {
                throw new CustomerNotFoundException(customerId);
            }
            Order order = orderRepository.save(
                    Order
                            .builder()
                            .customer(customerOptional.get())
                            .orderStatus(OrderStatus.PENDING)
                            .deliveryAddress(orderRequest.deliveryAddress())
                            .build()
            );
            var orderItemList = orderRequest.items().stream().map(i -> OrderItem.from(i, order.getId())).collect(Collectors.toList());
            orderItemRepository.saveAll(orderItemList);
            order.setBillingAmount(calculateBill(orderRequest.items()));
            order.setOrderStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);
            orderCreationEvent(order, orderRequest);
            return Order.from(order);
        } catch (Exception e) {
            log.error("issue with creating the order ", e);
            throw new OrderCreationException("issue with creating the order. message : " + e.getMessage());
        }
    }

    private BigDecimal calculateBill(List<Items> items) {
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

    private void orderCreationEvent(Order order, OrderRequest orderRequest) {
        try {
            var orderDetails = new OrderDetails(
                    order.getId(),
                    order.getCustomer().getId(),
                    orderRequest.restaurantId(),
                    order.getDeliveryAddress(),
                    order.getBillingAmount(),
                    orderRequest.items()
            );
            eventService.sendOrderEvent(orderDetails);
        } catch (Exception e) {
            // we can log the failed events in a database and retry in a scheduled manner, if we don't want the API to be failed
            log.error("order event is failed", e);
        }
    }

}
