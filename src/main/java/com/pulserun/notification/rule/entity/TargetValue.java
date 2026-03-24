package com.pulserun.notification.rule.entity;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.error.exception.PulserunException;
import com.pulserun.global.utils.Asserts;

import java.math.BigDecimal;

public record TargetValue(BigDecimal value) {
    private static final BigDecimal LOWER_BOUND = BigDecimal.valueOf(0);
    private static final BigDecimal UPPER_BOUND = BigDecimal.valueOf(10000000000.0);

    public TargetValue {
        Asserts.notNull(value, ErrorCode.DATA_IS_NULL);
        assertInRange(value);
    }

    public boolean isSatisfied(ConditionType conditionType, BigDecimal currentPrice) {
        int result = currentPrice.compareTo(this.value);
        return switch (conditionType) {
            case GREATER -> result > 0;
            case LESS -> result < 0;
            case GREATER_OR_EQUAL -> result >= 0;
            case LESS_OR_EQUAL -> result <= 0;
        };
    }

    private void assertInRange(BigDecimal value) {
        if (value.compareTo(LOWER_BOUND) < 0 || value.compareTo(UPPER_BOUND) > 0) {
            throw new PulserunException(ErrorCode.TARGET_VALUE_OUT_OF_RANGE);
        }
    }
}
