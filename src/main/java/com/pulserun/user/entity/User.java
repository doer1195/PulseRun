package com.pulserun.user.entity;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.utils.Asserts;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Builder
    public User(Long id, String provider, String providerId, String email, String nickname) {
        Asserts.notNull(provider, ErrorCode.DATA_IS_NULL, "provider");
        Asserts.notNull(providerId, ErrorCode.DATA_IS_NULL, "providerId");

        this.id = id;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.nickname = nickname;
    }
}
