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
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

/**
 * @author Krishna Chaitanya
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final EventService eventService;
    private final InvoiceService invoiceService;
    private final OrderCalculationService orderCalculationService;
    private final OrderVerificationService orderVerificationService;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    public Optional<OrderResponse> requestOrder(OrderRequest orderRequest) {

        try {
            // verify customer and order items information
            orderVerificationService.verifyOrder(orderRequest);

            // create order
            Order order = createOrder(orderRequest);

            // create order items and calculate the total amount
            BigDecimal calculatedBill = createOrderItemsAndCalculateTotal(orderRequest, order);

            order.setBillingAmount(calculatedBill);
            order.setOrderStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);

            // send order details to invoice service through an API call
            invoiceService.sendInvoice(OrderUtil.convertOrderRequestToOrderDetails(order, orderRequest));

            // publish order details event
            publishOrderCreationEvent(order, orderRequest);

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

    private Order createOrder(OrderRequest orderRequest) {
        return orderRepository.save(
                Order
                        .builder()
                        .customerId(orderRequest.customerId())
                        .orderStatus(OrderStatus.PENDING)
                        .deliveryAddress(orderRequest.deliveryAddress())
                        .build()
        );
    }

    private BigDecimal createOrderItemsAndCalculateTotal(OrderRequest orderRequest, Order order) {
        var orderItemList = orderRequest.items().stream().map(i -> OrderItem.from(i, order.getId())).toList();
        orderItemRepository.saveAll(orderItemList);
        return orderCalculationService.calculateBill(orderRequest.items());
    }


    private void publishOrderCreationEvent(Order order, OrderRequest orderRequest) {
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
