package com.pulserun.wishlist;


import com.pulserun.market.Market;
import com.pulserun.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "markets_id")
    private Market market;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    private LocalDateTime createdAt;

    @Builder
    public Wishlist(User user, Market market) {
        this.user = user;
        this.market = market;
        this.createdAt = LocalDateTime.now(); // 찜한 시간 자동 저장
    }

}
