package com.assessment.restaurant.controller;

import com.assessment.restaurant.dto.*;
import com.assessment.restaurant.service.*;
import jakarta.validation.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author Krishna Chaitanya
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceResponse> orderInvoice(@Valid @RequestBody OrderDetails orderDetails) {
        return ResponseEntity
                .status(OK)
                .body(invoiceService.orderInvoice(orderDetails));
    }

}
