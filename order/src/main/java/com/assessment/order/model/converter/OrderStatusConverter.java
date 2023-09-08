package com.assessment.order.model.converter;

import com.assessment.order.dto.*;
import jakarta.persistence.*;

/**
 * @author Krishna Chaitanya
 */
@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        return orderStatus.name();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        return OrderStatus.valueOf(dbData);
    }
}
