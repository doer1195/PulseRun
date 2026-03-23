package com.pulserun.notification.rule.entity;

import com.pulserun.user.entity.User;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;

public class RuleFixture {
    public static Rule.RuleBuilder createValidBuilder() {
        return Rule.builder()
                   .user(Mockito.mock(User.class))
                   .symbol("BTCUSDT")
                   .indicatorType(IndicatorType.PRICE)
                   .targetValue(new TargetValue(BigDecimal.valueOf(100)))
                   .conditionType(ConditionType.GREATER)
                   .extraParams(Map.of());
    }
}