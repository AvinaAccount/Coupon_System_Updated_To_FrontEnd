package com.Avinadav.couponsystem.data;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;


/**
 * A class that convert entity date into database date column and back again.
 */

@Converter
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        return attribute == null ? null : java.sql.Date.valueOf(attribute);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        return dbData == null ? null : dbData.toLocalDate();
    }
}
