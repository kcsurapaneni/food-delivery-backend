package com.assessment.order.controller;

import com.assessment.order.dto.*;
import com.assessment.order.exception.order.*;
import com.assessment.order.service.*;
import jakarta.validation.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
                .status(HttpStatus.CREATED)
                .body(optionalOrderResponse.get());
    }

}
