package com.assessment.restaurant.service;

import com.assessment.restaurant.dto.*;
import com.assessment.restaurant.model.*;
import com.assessment.restaurant.repository.*;
import lombok.*;
import org.springframework.stereotype.*;

/**
 * @author Krishna Chaitanya
 */
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceResponse orderInvoice(final OrderDetails orderDetails) {
        var invoice = invoiceRepository.save(
                Invoice
                        .builder()
                        .orderId(orderDetails.orderId())
                        .invoiceAmount(orderDetails.billingAmount())
                        .build()
        );
        return new InvoiceResponse(invoice.getId(), invoice.getInvoiceAmount(), "successful");
    }
}
