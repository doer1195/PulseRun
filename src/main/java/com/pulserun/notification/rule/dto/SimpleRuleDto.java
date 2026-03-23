package com.pulserun.notification.rule.dto;

import com.pulserun.notification.rule.entity.ConditionType;
import com.pulserun.notification.rule.entity.IndicatorType;
import com.pulserun.notification.rule.entity.Rule;
import com.pulserun.notification.rule.entity.TargetValue;
import com.pulserun.user.entity.User;

import java.math.BigDecimal;

public record SimpleRuleDto(
        Long userId,
        String symbol,
        IndicatorType indicatorType,
        Double targetValue,
        ConditionType conditionType
) {

    public Rule toEntity(User user) {
        return Rule.builder()
                   .user(user)
                   .symbol(this.symbol)
                   .indicatorType(this.indicatorType)
                   .targetValue(new TargetValue(BigDecimal.valueOf(this.targetValue)))
                   .conditionType(this.conditionType)
                   .extraParams(null) // 혹은 Map.of()
                   .build();
    }
}
