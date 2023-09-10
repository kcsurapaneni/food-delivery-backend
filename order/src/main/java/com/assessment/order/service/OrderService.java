package com.assessment.order.service;

import com.assessment.order.dto.*;
import com.assessment.order.exception.customer.*;
import com.assessment.order.exception.item.*;
import com.assessment.order.exception.order.*;
import com.assessment.order.model.*;
import com.assessment.order.repository.*;
import com.assessment.order.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

import java.math.*;
import java.util.*;

/**
 * @author Krishna Chaitanya
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${invoice.base.uri}")
    private String invoiceBaseUri;

    private final EventService eventService;

    private final RestTemplate restTemplate;

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
            var orderItemList = orderRequest.items().stream().map(i -> OrderItem.from(i, order.getId())).toList();
            orderItemRepository.saveAll(orderItemList);
            order.setBillingAmount(calculateBill(orderRequest.items()));
            order.setOrderStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);
            orderCreationEvent(order, orderRequest);
            var invoiceResponseResponseEntity = restTemplate.postForEntity(invoiceBaseUri, OrderUtil.convertOrderRequestToOrderDetails(order, orderRequest), InvoiceResponse.class);
            log.info("invoice api status code : {}, response : {}", invoiceResponseResponseEntity.getStatusCode(), invoiceResponseResponseEntity.getBody());
            return Order.from(order);
        } catch (CustomerNotFoundException e) {
            log.error("Customer is not found when creating the order", e);
            throw e;
        } catch (ItemNotFoundException e) {
            log.error("Item is not found when creating the order", e);
            throw e;
        } catch (Exception e) {
            log.error("Issue with creating the order", e);
            throw new OrderCreationException("Issue with creating the order. message : " + e.getMessage());
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
            var orderDetails = OrderUtil.convertOrderRequestToOrderDetails(order, orderRequest);
            eventService.sendOrderEvent(orderDetails);
        } catch (Exception e) {
            // we can log the failed events in a database and retry in a scheduled manner, if we don't want the API to be failed
            log.error("order event is failed", e);
        }
    }

    public OrderStatusResponse fetchOrderStatus(final Integer orderId) {
        var optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }
        return new OrderStatusResponse(optionalOrder.get().getId(), optionalOrder.get().getOrderStatus());
    }

}
