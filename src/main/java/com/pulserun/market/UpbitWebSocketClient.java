package com.pulserun.market;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import okio.ByteString;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class UpbitWebSocketClient extends WebSocketListener {

    private final SimpMessagingTemplate messagingTemplate; // STOMP 쏴주는 도구
    private final OkHttpClient client = new OkHttpClient();

    @PostConstruct
    public void connect() {
        Request request = new Request.Builder()
                .url("wss://api.upbit.com/websocket/v1")
                .build();
        client.newWebSocket(request, this);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        String json = "[{\"ticket\":\"pulse-run\"},{\"type\":\"ticker\",\"codes\":[\"KRW-BTC\",\"KRW-XRP\"]}]";
        webSocket.send(json);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        String message = bytes.utf8();

        messagingTemplate.convertAndSend("/topic/tickers", message);
    }
}