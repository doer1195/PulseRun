package com.pulserun.market;

import com.pulserun.engine.RuleEngine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpbitWebSocketClient extends BinaryWebSocketHandler {

    private final RuleEngine ruleEngine;
    private final ObjectMapper objectMapper;
    private static final String UPBIT_WEBSOCKET_URL = "wss://api.upbit.com/websocket/v1";

    @PostConstruct
    public void connect() {
        StandardWebSocketClient client = new StandardWebSocketClient();
        try {
            client.execute(this, UPBIT_WEBSOCKET_URL);
            log.info("[Market] Upbit 웹소켓 서버 연결 시도 중...");
        } catch (Exception e) {
            log.error("[Market] 웹소켓 연결 실패: {}", e.getMessage());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String subscribeMessage = "[{\"ticket\":\"UNIQUE_TICKET\"},{\"type\":\"ticker\",\"codes\":[\"KRW-BTC\"]}]";
        session.sendMessage(new TextMessage(subscribeMessage));
        log.info("[Market] Upbit 구독 메시지 전송 완료: {}", subscribeMessage);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            String payload = StandardCharsets.UTF_8.decode(message.getPayload()).toString();
            JsonNode jsonNode = objectMapper.readTree(payload);

            if (jsonNode.has("code") && jsonNode.has("trade_price")) {
                String symbol = jsonNode.get("code").asText().replace("KRW-", "");
                Double currentPrice = jsonNode.get("trade_price").asDouble();

                log.debug("[Market] Received: {} / {}", symbol, currentPrice);

                ruleEngine.evaluate(symbol, currentPrice);
            }
        } catch (Exception e) {
            log.error("[Market] 데이터 처리 중 예외 발생: {}", e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("[Market] 웹소켓 통신 에러: {}", exception.getMessage());
    }
}