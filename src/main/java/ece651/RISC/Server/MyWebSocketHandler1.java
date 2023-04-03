package ece651.RISC.Server;

import ece651.RISC.Server.config.WebSocketEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单的文本消息处理器
 *
 * @author cloudgyb
 * @since 2022/4/4 19:01
 */
@Component
@WebSocketEndpoint("/ws1")
public class MyWebSocketHandler1 extends TextWebSocketHandler {
    /**
     * 保存连接的会话
     */
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("连接已建立，会话ID：" + session.getId() + "，客户端地址：" + session.getRemoteAddress());
        sessions.put(session.getId(),session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("接受到消息：" + message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("消息传输出错！");
        exception.printStackTrace();
    }

    /**
     * 连接关闭移除会话
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("连接被关闭，会话ID：" + session.getId() + "，客户端地址：" + session.getRemoteAddress());
        sessions.remove(session.getId());
    }

    /**
     * 向客户端推送消息
     *
     * @param msg 文本消息
     */
    public void pushMsg(String msg) {
        final Collection<WebSocketSession> webSocketSessions = sessions.values();
        final TextMessage textMessage = new TextMessage(msg);
        webSocketSessions.forEach(s -> {
            try {
                s.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
