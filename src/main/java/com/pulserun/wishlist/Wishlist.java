package com.pulserun.wishlist;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.utils.Asserts;
import com.pulserun.market.entity.Market;
import com.pulserun.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "wishlists")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Wishlist(User user, Market market) {
        Asserts.notNull(user, ErrorCode.DATA_IS_NULL, "user");
        Asserts.notNull(market, ErrorCode.DATA_IS_NULL, "market");

        this.user = user;
        this.market = market;
        this.createdAt = LocalDateTime.now();
    }
}
