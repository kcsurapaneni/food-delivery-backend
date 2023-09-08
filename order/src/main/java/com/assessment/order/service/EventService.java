package com.assessment.order.service;

import com.assessment.order.dto.*;
import com.assessment.order.event.*;
import lombok.*;
import org.springframework.stereotype.*;

/**
 * @author Krishna Chaitanya
 */
@Service
@RequiredArgsConstructor
public class EventService {

    private final OrderDetailsKafkaProducer orderDetailsKafkaProducer;

    public void sendOrderEvent(OrderDetails orderDetails) {
        orderDetailsKafkaProducer.writeToKafka(orderDetails);
    }

}
