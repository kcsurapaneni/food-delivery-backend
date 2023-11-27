package com.assessment.order.service;

import com.assessment.order.dto.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

/**
 * @author Krishna Chaitanya
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    @Value("${invoice.base.uri}")
    private String invoiceBaseUri;

//    private final RestTemplate restTemplate;
    private final RestClient restClient;

    public boolean sendInvoice(final OrderDetails orderDetails) {
        /**
        var invoiceResponseResponseEntity = restTemplate.postForEntity(invoiceBaseUri, orderDetails, InvoiceResponse.class);
        log.info("invoice api status code : {}, response : {}", invoiceResponseResponseEntity.getStatusCode(), invoiceResponseResponseEntity.getBody());
        return true;
         **/
        var invoiceResponseResponseEntity =
                restClient
                        .post()
                        .uri(invoiceBaseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(orderDetails)
                        .retrieve()
                        .toEntity(InvoiceResponse.class);
        log.info("invoice api status code : {}, response : {}", invoiceResponseResponseEntity.getStatusCode(), invoiceResponseResponseEntity.getBody());
        return true;

    }

}
