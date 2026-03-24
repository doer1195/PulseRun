package com.pulserun.market.dto;

public record PriceChangedEvent(
        String symbol,
        Double currentPrice
) {
}
