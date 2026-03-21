package com.pulserun.market;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MarketController {
    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/api/ticker/candles")
    public List<CandleDto> getCandles(
            @RequestParam String market,
            @RequestParam(defaultValue = "1") int unit,
            @RequestParam(defaultValue = "100") int count) {
        return marketService.getMinuteCandles(market, unit, count);
    }

    @GetMapping("/api/prices")
    public String getPrices() {
        return marketService.getCryptoPrices();
    }
}
