package com.pulserun.notification.rule.dto;

import com.pulserun.notification.rule.entity.ConditionType;
import com.pulserun.notification.rule.entity.IndicatorType;
import com.pulserun.notification.rule.entity.Rule;

public record RuleResponse(
        Long id,
        String symbol,
        IndicatorType indicatorType,
        Double targetValue,
        ConditionType conditionType,
        boolean isActive
) {

    public static RuleResponse from(Rule rule) {
        return new RuleResponse(
                rule.getId(),
                rule.getSymbol(),
                rule.getIndicatorType(),
                rule.getTargetValue().value().doubleValue(),
                rule.getConditionType(),
                rule.isActive()
        );
    }
}