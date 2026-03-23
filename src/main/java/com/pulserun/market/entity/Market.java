package com.pulserun.market.entity;

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
@Table(name = "markets")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public Market(String code, String name) {
        Asserts.notNull(code, ErrorCode.DATA_IS_NULL, "code");
        Asserts.notNull(name, ErrorCode.DATA_IS_NULL, "name");
        this.code = code;
        this.name = name;
    }
}
