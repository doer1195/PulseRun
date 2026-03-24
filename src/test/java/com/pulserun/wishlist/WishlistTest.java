package com.pulserun.wishlist;

import com.pulserun.global.error.exception.PulserunException;
import com.pulserun.market.entity.Market;
import com.pulserun.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WishlistTest {

    @Nested
    @DisplayName("위시리스트 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 유저와 마켓이 있으면 생성에 성공한다")
        void createSuccess() {
            User mockUser = Mockito.mock(User.class);
            Market mockMarket = Mockito.mock(Market.class);

            Wishlist wishlist = Wishlist.builder()
                                        .user(mockUser)
                                        .market(mockMarket)
                                        .build();

            assertThat(wishlist.getUser()).isEqualTo(mockUser);
            assertThat(wishlist.getMarket()).isEqualTo(mockMarket);
            assertThat(wishlist.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        }

        @Test
        @DisplayName("유저가 null이면 예외가 발생한다")
        void nullUserFail() {
            Market mockMarket = Mockito.mock(Market.class);

            assertThatThrownBy(() -> Wishlist.builder()
                                             .user(null)
                                             .market(mockMarket)
                                             .build())
                    .isInstanceOf(PulserunException.class);
        }

        @Test
        @DisplayName("마켓이 null이면 예외가 발생한다")
        void nullMarketFail() {
            User mockUser = Mockito.mock(User.class);

            assertThatThrownBy(() -> Wishlist.builder()
                                             .user(mockUser)
                                             .market(null)
                                             .build())
                    .isInstanceOf(PulserunException.class);
        }
    }
}