package com.pulserun.market.entity;

import com.pulserun.global.error.exception.PulserunException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MarketTest {

    @Nested
    @DisplayName("마켓 엔티티 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("code와 name이 모두 있으면 생성에 성공해야 한다")
        void createSuccess() {
            Market market = Market.builder()
                                  .code("KRW-BTC")
                                  .name("비트코인")
                                  .build();

            assertThat(market.getCode()).isEqualTo("KRW-BTC");
            assertThat(market.getName()).isEqualTo("비트코인");
        }

        @Test
        @DisplayName("code가 null이면 예외가 발생한다")
        void nullCodeFail() {
            assertThatThrownBy(() -> Market.builder()
                                           .code(null)
                                           .name("비트코인")
                                           .build())
                    .isInstanceOf(PulserunException.class);
        }

        @Test
        @DisplayName("name이 null이면 예외가 발생한다")
        void nullNameFail() {
            assertThatThrownBy(() -> Market.builder()
                                           .code("KRW-BTC")
                                           .name(null)
                                           .build())
                    .isInstanceOf(PulserunException.class);
        }
    }
}