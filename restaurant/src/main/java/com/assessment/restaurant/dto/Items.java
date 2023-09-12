package com.assessment.restaurant.dto;

import jakarta.validation.constraints.*;

/**
 * @author Krishna Chaitanya
 */
public record Items(
        @Min(1) int itemId,
        @Min(1) int quantity,
        String notes
) {
}
