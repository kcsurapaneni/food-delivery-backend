package com.assessment.order.dto;

import java.math.*;
import java.time.*;

/**
 * @author Krishna Chaitanya
 */
public record OrderResponse(int orderId, OrderStatus orderStatus, BigDecimal billingAmount, Instant timestamp) {
}
