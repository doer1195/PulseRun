package com.pulserun.notification.rule.entity;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.utils.Asserts;
import com.pulserun.notification.rule.infrastructure.TargetValueConverter;
import com.pulserun.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Getter
@Slf4j
@Table(name = "notification_rules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private IndicatorType indicatorType;

    @Convert(converter = TargetValueConverter.class)
    @Column(nullable = false)
    private TargetValue targetValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ConditionType conditionType;

    @Column(nullable = false)
    private boolean isActive = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> extraParams;

    @Builder
    public Rule(
            User user,
            String symbol,
            IndicatorType indicatorType,
            TargetValue targetValue,
            ConditionType conditionType,
            Map<String, Object> extraParams
    ) {
        assertNonNull(user, indicatorType, targetValue, conditionType);
        Asserts.hasText(symbol, ErrorCode.DATA_IS_BLANK, "symbol");

        this.user = user;
        this.symbol = symbol;
        this.indicatorType = indicatorType;
        this.targetValue = targetValue;
        this.conditionType = conditionType;
        this.extraParams = extraParams;
    }

    public boolean isSatisfied(BigDecimal currentPrice) {
        Asserts.notNull(currentPrice, ErrorCode.DATA_IS_NULL, "currentPrice");

        return targetValue.isSatisfied(conditionType, currentPrice);
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
    }

    private static void assertNonNull(
            User user,
            IndicatorType indicatorType,
            TargetValue targetValue,
            ConditionType conditionType
    ) {
        Asserts.notNull(user, ErrorCode.DATA_IS_NULL, "user");
        Asserts.notNull(indicatorType, ErrorCode.DATA_IS_NULL, "indicatorType");
        Asserts.notNull(targetValue, ErrorCode.DATA_IS_NULL, "targetValue");
        Asserts.notNull(conditionType, ErrorCode.DATA_IS_NULL, "conditionType");
    }
}