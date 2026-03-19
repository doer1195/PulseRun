package com.pulserun.market;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MarketService {
    private final RestTemplate restTemplate = new RestTemplate();

    public List<CandleDto> getMinuteCandles(String market, int unit, int count) {
        String url = String.format("https://api.upbit.com/v1/candles/minutes/%d?market=%s&count=%d",
                unit, market, count);

        CandleDto[] candles = restTemplate.getForObject(url, CandleDto[].class);
        return Arrays.asList(candles);
    }

    public String getCryptoPrices() {
        RestTemplate restTemplate = new RestTemplate();
        String marketUrl = "https://api.upbit.com/v1/market/all";

        List<Map<String, String>> markets = restTemplate.getForObject(marketUrl, List.class);

        String krwMarkets = markets.stream()
                .map(m -> m.get("market"))
                .filter(market -> market.startsWith("KRW-"))
                .collect(Collectors.joining(","));

        String tickerUrl = "https://api.upbit.com/v1/ticker?markets=" + krwMarkets;

        return restTemplate.getForObject(tickerUrl, String.class);
    }
}

