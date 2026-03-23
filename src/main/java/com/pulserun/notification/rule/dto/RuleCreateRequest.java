package com.pulserun.notification.rule.dto;

import com.pulserun.notification.rule.entity.ConditionType;
import com.pulserun.notification.rule.entity.IndicatorType;

public record RuleCreateRequest(
        String symbol,
        IndicatorType indicatorType,
        Double targetValue,
        ConditionType conditionType
) {
}