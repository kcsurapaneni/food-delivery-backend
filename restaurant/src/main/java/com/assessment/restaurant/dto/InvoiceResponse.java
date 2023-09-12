package com.assessment.restaurant.dto;

import java.math.*;

/**
 * @author Krishna Chaitanya
 */
public record InvoiceResponse(int invoiceId, BigDecimal invoiceAmount, String message) {
}
