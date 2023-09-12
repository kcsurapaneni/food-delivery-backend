package com.assessment.restaurant.model.converter;

import com.assessment.restaurant.dto.*;
import jakarta.persistence.*;

/**
 * @author Krishna Chaitanya
 */
@Converter(autoApply = true)
public class RestaurantStatusConverter implements AttributeConverter<RestaurantStatus, String> {

    @Override
    public String convertToDatabaseColumn(RestaurantStatus orderStatus) {
        return orderStatus.name();
    }

    @Override
    public RestaurantStatus convertToEntityAttribute(String dbData) {
        return RestaurantStatus.valueOf(dbData);
    }

}
