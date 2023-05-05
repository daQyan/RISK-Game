package ece651.RISC.Server.Controller;

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
 * WebSocketHandler that handles incoming text messages.
 * This handler is annotated with @WebSocketEndpoint so that it can be automatically
 * registered with the Spring WebSocketHandlerRegistry.
 * <p>
 * This handler maintains a ConcurrentHashMap of WebSocketSession objects
 * that represent active WebSocket sessions. It also provides a method to send a text message
 * to all connected clients.
 */
@Component
@WebSocketEndpoint("/ws")
public class MyWebSocketHandler extends TextWebSocketHandler {
    /**
     * A ConcurrentHashMap of WebSocketSession objects that represent active WebSocket sessions.
     * The key is the session ID, and the value is the WebSocketSession object.
     */
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * Overrides the afterConnectionEstablished method of TextWebSocketHandler.
     * This method is called after the WebSocket connection is established.
     * It adds the session to the sessions map.
     *
     * @param session the WebSocketSession object
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("连接已建立，会话ID：" + session.getId() + "，客户端地址：" + session.getRemoteAddress());
        sessions.put(session.getId(), session);
    }

    /**
     * Overrides the handleTextMessage method of TextWebSocketHandler.
     * This method is called when a text message is received.
     *
     * @param session the WebSocketSession object
     * @param message the TextMessage object
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("接受到消息：" + message.getPayload());
    }

    /**
     * Overrides the handleTransportError method of TextWebSocketHandler.
     * This method is called when a transport error occurs.
     *
     * @param session the WebSocketSession object
     * @param exception the Throwable object representing the error
     */

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("消息传输出错！");
        exception.printStackTrace();
    }

    /**
     * Overrides the afterConnectionClosed method of TextWebSocketHandler.
     * This method is called when the WebSocket connection is closed.
     * It removes the session from the sessions map.
     *
     * @param session the WebSocketSession object
     * @param status the CloseStatus object representing the reason for the closure
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("连接被关闭，会话ID：" + session.getId() + "，客户端地址：" + session.getRemoteAddress());
        sessions.remove(session.getId());
    }

    /**
     * Sends a text message to all connected clients.
     *
     * @param msg the text message
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
