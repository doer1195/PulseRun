package com.pulserun.notification.rule.entity;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.error.exception.PulserunException;
import com.pulserun.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RuleTest {

    private User validUser;
    private final String validSymbol = "BTCUSDT";
    private final IndicatorType validIndicator = IndicatorType.PRICE;
    private final TargetValue validTarget = new TargetValue(BigDecimal.valueOf(100000.0));
    private final ConditionType validCondition = ConditionType.GREATER;

    @BeforeEach
    void setUp() {
        validUser = Mockito.mock(User.class);
    }

    @Test
    @DisplayName("모든 파라미터가 정상이면 Rule 객체가 정상적으로 생성된다")
    void createRule_Success() {
        Rule rule = Rule.builder()
                        .user(validUser)
                        .symbol(validSymbol)
                        .indicatorType(validIndicator)
                        .targetValue(validTarget)
                        .conditionType(validCondition)
                        .build();

        assertThat(rule.getSymbol()).isEqualTo(validSymbol);
        assertThat(rule.getTargetValue()).isEqualTo(validTarget);
    }

    @Test
    @DisplayName("User가 null이면 예외가 발생한다")
    void createRule_NullUser() {
        assertThatThrownBy(() -> RuleFixture.createValidBuilder().user(null).build())
                .isInstanceOf(PulserunException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DATA_IS_NULL);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Symbol이 null이거나 공백이면 예외가 발생한다")
    void createRule_InvalidSymbol(String invalidSymbol) {
        assertThatThrownBy(() -> RuleFixture.createValidBuilder().symbol(invalidSymbol).build())
                .isInstanceOf(PulserunException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DATA_IS_BLANK);
    }

    @Test
    @DisplayName("IndicatorType이 null이면 예외가 발생한다")
    void createRule_NullIndicator() {
        assertThatThrownBy(() -> RuleFixture.createValidBuilder().indicatorType(null).build())
                .isInstanceOf(PulserunException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DATA_IS_NULL);
    }

    @Test
    @DisplayName("TargetValue가 null이면 예외가 발생한다")
    void createRule_NullTargetValue() {
        assertThatThrownBy(() -> RuleFixture.createValidBuilder().targetValue(null).build())
                .isInstanceOf(PulserunException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DATA_IS_NULL);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -100.5})
    @DisplayName("TargetValue가 음수면 예외가 발생한다")
    void createRule_NegativeOrZeroTargetValue(Double invalidTarget) {
        assertThatThrownBy(() -> RuleFixture.createValidBuilder()
                                            .targetValue(new TargetValue(BigDecimal.valueOf(invalidTarget)))
                                            .build())
                .isInstanceOf(PulserunException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.TARGET_VALUE_OUT_OF_RANGE);
    }

    @Test
    @DisplayName("ConditionType이 null이면 예외가 발생한다")
    void createRule_NullCondition() {
        assertThatThrownBy(() -> RuleFixture.createValidBuilder()
                                            .conditionType(null)
                                            .build())
                .isInstanceOf(PulserunException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DATA_IS_NULL);
    }

    @Test
    @DisplayName("현재가격이 null이면 PulseRunException 발생한다")
    void isSatisfiedNotNull() {
        Rule rule = RuleFixture.createValidBuilder().build();

        assertThatThrownBy(() -> rule.isSatisfied(null)).isInstanceOf(PulserunException.class);
    }

    @Test
    @DisplayName("isActive의 값이 메서드를 통해 토글되는지 확인한다")
    void toggleActive() {
        Rule rule = RuleFixture.createValidBuilder().build();

        rule.toggleActive();
        assertThat(rule.isActive()).isFalse();

        rule.toggleActive();
        assertThat(rule.isActive()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "GREATER,          100.0, 101.0, true",
            "GREATER,          100.0, 100.0, false",
            "GREATER_OR_EQUAL, 100.0, 100.0, true",
            "LESS,             100.0, 99.0,  true",
            "LESS,             100.0, 100.0, false",
            "LESS_OR_EQUAL,    100.0, 100.0, true"
    })
    @DisplayName("현재가와 목표가와 조건이 주어졌을 때의 로직 검증한다")
    void isSatisfied_AllCases(String type, Double target, Double current, boolean expected) {
        TargetValue targetValue = new TargetValue(BigDecimal.valueOf(target));
        BigDecimal currentValue = new BigDecimal(current);
        Rule rule = RuleFixture.createValidBuilder()
                               .targetValue(targetValue)
                               .conditionType(ConditionType.valueOf(type))
                               .build();

        assertThat(rule.isSatisfied(currentValue)).isEqualTo(expected);
    }
}