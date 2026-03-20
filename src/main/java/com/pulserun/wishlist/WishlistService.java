package com.pulserun.wishlist;

import com.pulserun.market.Market;
import com.pulserun.market.MarketRepository;
import com.pulserun.user.User;
import com.pulserun.user.UserRepository;

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

    @Transactional
    public void addWish(Long providerId, Long marketId) {
        User user = userRepository.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException("user does not exist"));
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new IllegalArgumentException("market does not exist"));

        if (wishlistRepository.existsByUserAndMarket(user, market)) {
            throw new IllegalStateException("already added");
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
                .orElseThrow(() -> new IllegalArgumentException("user does not exist"));
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new IllegalArgumentException("market does not exist"));

        Wishlist wishlist = wishlistRepository.findByUserAndMarket(user, market)
                .orElseThrow(() -> new IllegalArgumentException("already removed"));

        wishlistRepository.delete(wishlist);
    }

    public List<Wishlist> getMyWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user does not exist"));

        return wishlistRepository.findAllByUser(user);
    }
}