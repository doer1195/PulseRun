package com.pulserun.notification.rule;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.error.exception.BusinessException;
import com.pulserun.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String indicatorType;

    private boolean isActive = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> conditions;

    @Builder
    public Rule(User user, String symbol, String indicatorType, Map<String, Object> conditions) {
        this.user = user;
        this.symbol = symbol;
        this.indicatorType = indicatorType;
        this.conditions = conditions;
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
    }

    public boolean isSatisfied(Double currentPrice) {
        if (isInvalidInput(currentPrice)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        Double targetValue = extractTargetValue();
        String conditionType = extractConditionType();

        return calculate(currentPrice, targetValue, conditionType);
    }

    private boolean isInvalidInput(Double currentPrice) {
        return currentPrice == null || this.conditions == null;
    }

    private Double extractTargetValue() {
        Object value = conditions.get("targetValue");
        if (value == null) {
            throw new IllegalArgumentException("targetValue가 없습니다.");
        }
        return Double.valueOf(value.toString());
    }

    private String extractConditionType() {
        String type = (String) conditions.get("condition");
        if (type == null) {
            throw new IllegalArgumentException("condition 타입이 없습니다.");
        }
        return type.toUpperCase();
    }

    private boolean calculate(Double currentPrice, Double targetValue, String conditionType) {
        if ("ABOVE".equals(conditionType)) {
            return true;
        }
        if ("BELOW".equals(conditionType)) {
            return false;
        }
        throw new IllegalArgumentException("");
    }
}