package ece651.RISC.Server.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import ece651.RISC.Server.config.WebSocketEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket handler for the chat room.
 * Handles incoming WebSocket messages and broadcasts them to all connected users.
 * Also maintains a map of WebSocketSessions for each player to enable private messaging.
 */

/**
 * 这里，我们维护了一个 Map<String, WebSocketSession> 对象，它将每个 player 的 id 映射到其对应的 WebSocketSession。
 * 在 afterConnectionEstablished 回调中，我们从 WebSocket 连接的 URI 中解析出 player id 并将其加入到 playerSessions 映射中。
 * 在 handleTextMessage 回调中，我们检查接收到的消息是否包含空格，如果不包含，就将消息广播给所有连接的用户；
 * 如果包含空格，则我们将第一个空格之前的部分解析为接收方的 player id，并使用
 */
@Component
@WebSocketEndpoint("/{gameID}/chat")
public class ChatWebSocketHandler extends TextWebSocketHandler {
    // Maintain a list of all currently connected WebSocket sessions
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    // Maintain a map of WebSocket sessions for each player
    private final Map<String, WebSocketSession> playerSessions = new HashMap<>();

    /**
     * Callback for when a new WebSocket connection is established.
     * Adds the new session to the list of connected sessions.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
//        String playerId = session.getUri().getQuery().split("=")[1];
        String uri = session.getUri().getPath();
        String[] uriParts = uri.split("/");
        String gameId = uriParts[1];
        String playerId = session.getUri().getQuery().split("=")[1];
        playerSessions.put(playerId, session);
        String broadcastMessage = "Game " + gameId + ", Player " + playerId + " join the chat";
        broadcast(broadcastMessage, gameId);

    }

    /**
     * Callback for when a WebSocket message is received.
     * Broadcasts the message to all connected users or sends a private message to a specific player.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String playerId = session.getUri().getQuery().split("=")[1];
        String gameId = session.getUri().getPath().split("/")[1];
        String broadcastMessage = "Game " + gameId + ", Player " + playerId + ": " + payload;
        broadcast(broadcastMessage, gameId);


    }


    /**
     * Callback for when a WebSocket connection is closed.
     * Removes the session from the list of connected sessions.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
//        String playerId = session.getUri().getQuery().split("=")[1];
        String uri = session.getUri().getPath();
        String[] uriParts = uri.split("/");
        String gameId = uriParts[1];
        String playerId = session.getUri().getQuery().split("=")[1];

        playerSessions.remove(playerId);
        String broadcastMessage = "Game " + gameId + ", Player " + playerId + " left the chat";
        broadcast(broadcastMessage, gameId);

    }

    private void broadcast(String message, String gameId) throws IOException {
        for (WebSocketSession session : sessions) {
            String uri = session.getUri().getPath();
            String[] uriParts = uri.split("/");
            String sessionGameId = uriParts[1];
            if (sessionGameId.equals(gameId)) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }


}
