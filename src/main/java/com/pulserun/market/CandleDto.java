package com.pulserun.market;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandleDto {
    private String market;            // 종목 코드 (KRW-BTC)
    private String candle_date_time_kst; // 기준 시간
    private double opening_price;     // 시가
    private double high_price;        // 고가
    private double low_price;         // 저가
    private double trade_price;       // 종가
    private double candle_acc_trade_volume; // 거래량
}