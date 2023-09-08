package com.assessment.order.exception.item;

import org.springframework.http.*;
import org.springframework.web.*;

/**
 * @author Krishna Chaitanya
 */
public class ItemNotFoundException extends ErrorResponseException {

    public ItemNotFoundException(Integer id) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("Item with id " + id + " not found"), null);
    }

    private static ProblemDetail asProblemDetail(final String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("Item information is not found");
        return problemDetail;
    }

}
