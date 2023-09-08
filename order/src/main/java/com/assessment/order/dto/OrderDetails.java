package com.assessment.order.dto;

import java.math.*;
import java.util.*;

/**
 * @author Krishna Chaitanya
 */
public record OrderDetails(int orderId, int customerId, int restaurantId, String deliveryAddress, BigDecimal billingAmount, List<Items> items) {
}
