package com.assessment.restaurant.dto;

import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.*;

import java.math.*;
import java.util.*;

/**
 * @author Krishna Chaitanya
 */
@Validated
public record OrderDetails(
        @Min(1) int orderId,
        @Min(1) int customerId,
        @Min(1) int restaurantId,
        @NotBlank String deliveryAddress,
        @Min(1) BigDecimal billingAmount,
        @Size(min = 1) List<Items> items) {
}
