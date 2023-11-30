package com.example.mealserve.global.websocket;

import com.example.mealserve.global.exception.CustomException;
import com.example.mealserve.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Socket 연결");
        String senderAccount = searchAccount(session);
        log.info("{}", senderAccount);
        sessionMap.put(senderAccount, session);
    }

    public void sendNotification(String email, String message) {
        // 세션맵에서 사용자의 웹소켓 세션을 얻어냄
        WebSocketSession session = sessionMap.get(email);
        // 해당 세션이 존재하고 열려있는 경우, 메시지를 전송
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Socket 연결 해제");
        sessionMap.remove(searchAccount(session), session);
    }

    private String searchAccount(WebSocketSession session) {
        String loginEmail;

        if (session.getPrincipal() == null) {
            throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
        } else {
            loginEmail = session.getPrincipal().getName();
        }
        return loginEmail;
    }
}
