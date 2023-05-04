package ece651.RISC.Server.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ece651.RISC.Server.config.WebSocketEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.HashMap;
import java.util.Map;
@Component
@WebSocketEndpoint("/{gameID}/chat")
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> playerSessions = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String playerId = getPlayerIdFromSessionUri(session.getUri().getQuery());
        String gameId = getGameIdFromSessionUri(session.getUri().getPath());
        playerSessions.put(playerId, session);
        String broadcastMessage = String.format("Game %s, Player %s join the chat", gameId, playerId);
        broadcast(broadcastMessage, gameId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String playerId = getPlayerIdFromSessionUri(session.getUri().getQuery());
        String gameId = getGameIdFromSessionUri(session.getUri().getPath());

        // Parse the message as JSON
        Map<String, Object> messageJson = objectMapper.readValue(payload, HashMap.class);
        int id = (int) messageJson.get("playerID");
        String text = messageJson.get("text message").toString();

        // Broadcast the message as a JSON object
        Map<String, Object> broadcastJson = new HashMap<>();
        broadcastJson.put("gameID", gameId);
        broadcastJson.put("playerID", id);
        broadcastJson.put("text message", text);
        String broadcastMessage = objectMapper.writeValueAsString(broadcastJson);
        broadcast(broadcastMessage, gameId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String playerId = getPlayerIdFromSessionUri(session.getUri().getQuery());
        String gameId = getGameIdFromSessionUri(session.getUri().getPath());
        playerSessions.remove(playerId);
        String broadcastMessage = String.format("Game %s, Player %s left the chat", gameId, playerId);
        broadcast(broadcastMessage, gameId);
    }

    private String getPlayerIdFromSessionUri(String uriQuery) {
        return uriQuery.split("=")[1];
    }

    private String getGameIdFromSessionUri(String uriPath) {
        return uriPath.split("/")[1];
    }

    private void broadcast(String message, String gameId) throws Exception {
        for (WebSocketSession session : playerSessions.values()) {
            String sessionGameId = getGameIdFromSessionUri(session.getUri().getPath());
            if (sessionGameId.equals(gameId)) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}
