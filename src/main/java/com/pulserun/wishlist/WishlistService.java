package com.pulserun.wishlist;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.error.exception.PulserunException;
import com.pulserun.market.entity.Market;
import com.pulserun.market.repository.MarketRepository;
import com.pulserun.user.entity.User;
import com.pulserun.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final MarketRepository marketRepository;

    public List<Wishlist> getMyWishlist(Long userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new PulserunException(ErrorCode.USER_NOT_FOUND));

        return wishlistRepository.findAllByUser(user);
    }

    @Transactional
    public void addWish(Long userId, Long marketId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new PulserunException(ErrorCode.USER_NOT_FOUND));
        Market market = marketRepository.findById(marketId)
                                        .orElseThrow(() -> new PulserunException(ErrorCode.MARKET_NOT_FOUND));

        if (wishlistRepository.existsByUserAndMarket(user, market)) {
            throw new PulserunException(ErrorCode.WISHLIST_ALREADY_EXIST);
        }

        Wishlist wishlist = Wishlist.builder()
                                    .user(user)
                                    .market(market)
                                    .build();

        wishlistRepository.save(wishlist);
    }

    @Transactional
    public void removeWish(Long userId, Long marketId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new PulserunException(ErrorCode.USER_NOT_FOUND));
        Market market = marketRepository.findById(marketId)
                                        .orElseThrow(() -> new PulserunException(ErrorCode.MARKET_NOT_FOUND));

        Wishlist wishlist = wishlistRepository.findByUserAndMarket(user, market)
                                              .orElseThrow(()-> new PulserunException(ErrorCode.WISHLIST_NOT_FOUND));

        wishlistRepository.delete(wishlist);
    }
}