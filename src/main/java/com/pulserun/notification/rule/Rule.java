package com.pulserun.notification.rule;

import com.pulserun.user.User;
import jakarta.persistence.*;
import lombok.*;
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
        if (currentPrice == null || this.conditions == null) {
            return false;
        }

        try {
            Object targetObj = conditions.get("targetValue");
            Double targetValue = Double.valueOf(targetObj.toString());

            String conditionType = (String) conditions.get("condition"); // "ABOVE" 또는 "BELOW"

            if (conditionType == null) {
                return false;
            }

            return switch (conditionType.toUpperCase()) {
                case "ABOVE" -> currentPrice >= targetValue;
                case "BELOW" -> currentPrice <= targetValue;
                default -> {
                    log.warn("[Rule Logic] 알 수 없는 조건 타입입니다: {}", conditionType);
                    yield false;
                }
            };
        } catch (Exception e) {
            log.error("[Rule Logic] 조건 평가 중 오류 발생. Rule ID: {}, Error: {}", this.id, e.getMessage());
            return false;
        }
    }
}