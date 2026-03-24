package com.pulserun.user.entity;

import com.pulserun.global.error.exception.PulserunException;
import com.pulserun.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Nested
    @DisplayName("유저 엔티티 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("필수 값이 모두 있으면 생성에 성공해야 한다")
        void createSuccess() {
            User user = User.builder()
                            .provider("google")
                            .providerId("12345")
                            .email("test@test.com")
                            .nickname("tester")
                            .build();

            assertThat(user.getProvider()).isEqualTo("google");
            assertThat(user.getProviderId()).isEqualTo("12345");
            assertThat(user.getEmail()).isEqualTo("test@test.com");
            assertThat(user.getNickname()).isEqualTo("tester");
        }

        @Test
        @DisplayName("provider가 null이면 예외가 발생한다")
        void nullProviderFail() {
            assertThatThrownBy(() -> User.builder()
                                         .provider(null)
                                         .providerId("12345")
                                         .build())
                    .isInstanceOf(PulserunException.class);
        }

        @Test
        @DisplayName("providerId가 null이면 예외가 발생한다")
        void nullProviderIdFail() {
            assertThatThrownBy(() -> User.builder()
                                         .provider("google")
                                         .providerId(null)
                                         .build())
                    .isInstanceOf(PulserunException.class);
        }
    }
}