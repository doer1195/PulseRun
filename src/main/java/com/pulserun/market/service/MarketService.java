package com.pulserun.market.service;

import com.pulserun.market.dto.CandleDto;
import com.pulserun.market.dto.UpbitMarketDto;
import com.pulserun.market.entity.Market;
import com.pulserun.market.infrastructure.UpbitClient;
import com.pulserun.market.repository.MarketRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketService {

    private final UpbitClient upbitClient;
    private final MarketRepository marketRepository;

    @PostConstruct
    @Transactional
    public void syncUpbitMarkets() {
        log.info("🚀 업비트 마켓 데이터 동기화 시작...");

        List<UpbitMarketDto> upbitMarkets = upbitClient.fetchAllMarkets();

        if (upbitMarkets == null || upbitMarkets.isEmpty()) {
            log.warn("⚠️ 데이터를 가져오지 못했습니다.");
            return;
        }

        long count = upbitMarkets.stream()
                                 .filter(dto -> dto.market().startsWith("KRW-"))
                                 .filter(dto -> !marketRepository.existsByCode(dto.market()))
                                 .map(dto -> Market.builder()
                                                   .code(dto.market())
                                                   .name(dto.koreanName())
                                                   .build())
                                 .peek(marketRepository::save)
                                 .count();

        log.info("✅ 동기화 완료! (신규 추가: {})", count);
    }

    public List<CandleDto> getMinuteCandles(String market, int unit, int count) {
        return upbitClient.fetchMinuteCandles(market, unit, count);
    }

    public String getCryptoPrices() {
        List<UpbitMarketDto> markets = upbitClient.fetchAllMarkets();

        String krwMarkets = markets.stream()
                                   .map(UpbitMarketDto::market)
                                   .filter(m -> m.startsWith("KRW-"))
                                   .collect(Collectors.joining(","));

        return upbitClient.fetchTickers(krwMarkets);
    }
}