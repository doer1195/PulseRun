package com.pulserun.market.infrastructure;

import com.pulserun.market.dto.CandleDto;
import com.pulserun.market.dto.UpbitMarketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpbitClient {

    private final RestClient restClient = RestClient.create();
    private static final String BASE_URL = "https://api.upbit.com/v1";

    public List<UpbitMarketDto> fetchAllMarkets() {
        return restClient.get()
                         .uri(BASE_URL + "/market/all?isDetails=false")
                         .retrieve()
                         .body(new ParameterizedTypeReference<>() {});
    }

    public List<CandleDto> fetchMinuteCandles(String market, int unit, int count) {
        String uri = String.format("%s/candles/minutes/%d?market=%s&count=%d",
                                   BASE_URL, unit, market, count);
        return restClient.get()
                         .uri(uri)
                         .retrieve()
                         .body(new ParameterizedTypeReference<>() {});
    }

    public String fetchTickers(String markets) {
        return restClient.get()
                         .uri(BASE_URL + "/ticker?markets=" + markets)
                         .retrieve()
                         .body(String.class);
    }
}