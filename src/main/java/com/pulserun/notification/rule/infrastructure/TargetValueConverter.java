package com.pulserun.notification.rule.infrastructure;

import com.pulserun.notification.rule.entity.TargetValue;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class TargetValueConverter implements AttributeConverter<TargetValue, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(TargetValue targetValue) {
        if (targetValue == null) {
            return null;
        }

        return targetValue.value();
    }

    @Override
    public TargetValue convertToEntityAttribute(BigDecimal dbData) {
        if (dbData == null) {
            return null;
        }

        return new TargetValue(dbData);
    }
}
