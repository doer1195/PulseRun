package com.pulserun.user;

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
        this.id = id;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.nickname = nickname;
    }
}
