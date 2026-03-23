package com.pulserun.notification.rule.entity;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.error.exception.PulserunException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TargetValueTest {

    @Nested
    @DisplayName("생성 및 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("정상적인 값이 들어오면 객체가 생성")
        void createSuccess() {
            BigDecimal validValue = BigDecimal.valueOf(10000);
            TargetValue targetValue = new TargetValue(validValue);
            assertThat(targetValue.value()).isEqualTo(validValue);
        }

        @Test
        @DisplayName("null이 들어오면 에러가 발생해야 한다")
        void nullValueFail() {
            assertThatThrownBy(() -> new TargetValue(null))
                    .isInstanceOf(PulserunException.class);
        }

        @ParameterizedTest
        @DisplayName("범위를 벗어난 값이 들어오면 에러가 발생해야 한다")
        @CsvSource({"-1", "10000000000.1"})
        void outOfRangeFail(BigDecimal invalidValue) {
            assertThatThrownBy(() -> new TargetValue(invalidValue))
                    .isInstanceOf(PulserunException.class)
                    .hasMessageContaining(ErrorCode.TARGET_VALUE_OUT_OF_RANGE.getMessage());
        }
    }

    @Nested
    @DisplayName("조건 만족 여부(isSatisfied) 테스트")
    class LogicTest {

        private final TargetValue target = new TargetValue(BigDecimal.valueOf(100));

        @ParameterizedTest
        @DisplayName("각 조건에 따른 결과가 정확해야 한다")
        @CsvSource({
                "GREATER,          101, true",
                "GREATER,          100, false",
                "LESS,             99, true",
                "LESS,             100, false",
                "GREATER_OR_EQUAL, 100, true",
                "GREATER_OR_EQUAL, 99, false",
                "LESS_OR_EQUAL,    100, true",
                "LESS_OR_EQUAL,    101, false"
        })
        void isSatisfiedTest(ConditionType type, BigDecimal currentPrice, boolean expected) {
            boolean actual = target.isSatisfied(type, currentPrice);
            assertThat(actual).isEqualTo(expected);
        }
    }
}