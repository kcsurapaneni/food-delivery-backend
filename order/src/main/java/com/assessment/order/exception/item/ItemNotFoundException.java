package com.assessment.order.exception.item;

import org.springframework.http.*;
import org.springframework.web.*;

import java.util.*;

/**
 * @author Krishna Chaitanya
 */
public class ItemNotFoundException extends ErrorResponseException {

    public ItemNotFoundException(Integer id) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("Item with id " + id + " not found"), null);
    }

    public ItemNotFoundException(List<Integer> actual, List<Integer> expected) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("Available item id's : " + actual + ", expected item id's :  " + expected), null);
    }

    private static ProblemDetail asProblemDetail(final String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("Item information is not found");
        return problemDetail;
    }

}
