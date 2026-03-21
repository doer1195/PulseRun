package com.pulserun.market;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final MarketRepository marketRepository;
    private final RestClient restClient = RestClient.create();

    @PostConstruct
    @Transactional
    public void syncUpbitMarkets() {
        log.info("🚀 업비트 마켓 데이터 동기화 시작...");

        List<UpbitMarketDto> upbitMarkets = restClient.get()
                .uri("https://api.upbit.com/v1/market/all?isDetails=false")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (upbitMarkets == null || upbitMarkets.isEmpty()) {
            log.warn("⚠️ 업비트에서 데이터를 가져오지 못했습니다.");
            return;
        }

        int count = 0;
        for (UpbitMarketDto dto : upbitMarkets) {
            if (dto.market().startsWith("KRW-") && !marketRepository.existsByCode(dto.market())) {
                Market market = Market.builder()
                        .code(dto.market())
                        .name(dto.koreanName())
                        .build();
                marketRepository.save(market);
                count++;
            }
        }

        log.info("✅ 업비트 마켓 데이터 동기화 완료! (새로 추가된 종목 수: {})", count);
    }

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

