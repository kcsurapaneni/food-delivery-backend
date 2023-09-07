package com.assessment.order.dto;

import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.*;

import java.util.*;

/**
 * @author Krishna Chaitanya
 */
@Validated
public record OrderRequest(
        @Min(1) int restaurantId,
        @Min(1) int customerId,
        @NotBlank String deliveryAddress,
        @Size(min = 1) List<Items> items
) {
}
