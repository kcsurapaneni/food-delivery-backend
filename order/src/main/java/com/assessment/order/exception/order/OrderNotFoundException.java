package com.assessment.order.exception.order;

import org.springframework.http.*;
import org.springframework.web.*;

/**
 * @author Krishna Chaitanya
 */
public class OrderNotFoundException extends ErrorResponseException {

    public OrderNotFoundException(Integer id) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("order with id " + id + " not found"), null);
    }

    private static ProblemDetail asProblemDetail(final String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("Order information is not found");
        return problemDetail;
    }

}
