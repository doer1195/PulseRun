package com.pulserun.market;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpbitMarketDto(
        @JsonProperty("market") String market,
        @JsonProperty("korean_name") String koreanName,
        @JsonProperty("english_name") String englishName
) {
}