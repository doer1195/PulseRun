package com.pulserun.wishlist;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.error.exception.PulserunException;
import com.pulserun.market.entity.Market;
import com.pulserun.market.repository.MarketRepository;
import com.pulserun.user.entity.User;
import com.pulserun.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @InjectMocks
    private WishlistService wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MarketRepository marketRepository;

    @Nested
    @DisplayName("위시리스트 조회 테스트")
    class GetWishlist {
        @Test
        @DisplayName("유저의 위시리스트를 정상적으로 조회해야 한다")
        void getSuccess() {
            User user = mock(User.class);
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(wishlistRepository.findAllByUser(user)).willReturn(List.of(mock(Wishlist.class)));

            List<Wishlist> result = wishlistService.getMyWishlist(1L);

            assertThat(result).hasSize(1);
            verify(wishlistRepository, times(1)).findAllByUser(user);
        }

        @Test
        @DisplayName("존재하지 않는 유저라면 예외가 발생해야 한다")
        void userNotFound() {
            given(userRepository.findById(1L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> wishlistService.getMyWishlist(1L))
                    .isInstanceOf(PulserunException.class)
                    .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("위시리스트 추가 테스트")
    class AddWish {
        @Test
        @DisplayName("새로운 위시리스트를 정상적으로 추가해야 한다")
        void addSuccess() {
            User user = mock(User.class);
            Market market = mock(Market.class);
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(marketRepository.findById(2L)).willReturn(Optional.of(market));
            given(wishlistRepository.existsByUserAndMarket(user, market)).willReturn(false);

            wishlistService.addWish(1L, 2L);

            verify(wishlistRepository, times(1)).save(any(Wishlist.class));
        }

        @Test
        @DisplayName("이미 존재하는 위시리스트라면 예외가 발생해야 한다")
        void alreadyExist() {
            User user = mock(User.class);
            Market market = mock(Market.class);
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(marketRepository.findById(2L)).willReturn(Optional.of(market));
            given(wishlistRepository.existsByUserAndMarket(user, market)).willReturn(true);

            assertThatThrownBy(() -> wishlistService.addWish(1L, 2L))
                    .isInstanceOf(PulserunException.class)
                    .hasMessageContaining(ErrorCode.WISHLIST_ALREADY_EXIST.getMessage());
        }
    }

    @Nested
    @DisplayName("위시리스트 삭제 테스트")
    class RemoveWish {
        @Test
        @DisplayName("위시리스트를 정상적으로 삭제해야 한다")
        void removeSuccess() {
            User user = mock(User.class);
            Market market = mock(Market.class);
            Wishlist wishlist = mock(Wishlist.class);
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(marketRepository.findById(2L)).willReturn(Optional.of(market));
            given(wishlistRepository.findByUserAndMarket(user, market)).willReturn(Optional.of(wishlist));

            wishlistService.removeWish(1L, 2L);

            verify(wishlistRepository, times(1)).delete(wishlist);
        }
    }
}