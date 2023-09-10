package com.assessment.order.controller;

import com.assessment.order.dto.*;
import com.assessment.order.exception.order.*;
import com.assessment.order.service.*;
import jakarta.validation.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.HttpStatus.*;

/**
 * @author Krishna Chaitanya
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    ResponseEntity<OrderResponse> requestOrder(@Valid @RequestBody OrderRequest orderRequest) {
        Optional<OrderResponse> optionalOrderResponse = orderService.requestOrder(orderRequest);
        if (optionalOrderResponse.isEmpty()) {
            throw new OrderCreationException("Empty order is created");
        }
        return ResponseEntity
                .status(CREATED)
                .body(optionalOrderResponse.get());
    }

    @GetMapping("/status/{id}")
    ResponseEntity<OrderStatusResponse> requestOrder(@PathVariable("id") Integer orderId) {
        return ResponseEntity
                .status(OK)
                .body(orderService.fetchOrderStatus(orderId));
    }

}
