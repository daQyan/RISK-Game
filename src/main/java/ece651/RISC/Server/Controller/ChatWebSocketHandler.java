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

/**
 * This is the ChatWebSocketHandler class that handles incoming WebSocket messages related to chat feature for a game.
 * The WebSocket endpoint for this handler is defined in the @WebSocketEndpoint annotation on the class level.
 * This class extends the TextWebSocketHandler class provided by Spring WebSocket, which allows handling of text-based WebSocket messages.
 * The playerSessions map keeps track of all active WebSocket sessions of players that have joined the chat for a particular game.
 * The objectMapper is used to parse incoming JSON messages and to create JSON messages that are sent as broadcasts.
 */
@Component
@WebSocketEndpoint("/chat")
public class ChatWebSocketHandler extends TextWebSocketHandler {
    /**
     * The playerSessions map is a HashMap that keeps track of all active WebSocket sessions of players that have joined the chat for a particular game.
     * The key is the player ID and the value is the corresponding WebSocket session object.
     */
    private final Map<String, WebSocketSession> playerSessions = new HashMap<>();
    /**
     * The objectMapper is used to parse incoming JSON messages and to create JSON messages that are sent as broadcasts.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * This method is called after a new WebSocket connection is established.
     * It adds the player's WebSocket session to the playerSessions map and sends a broadcast message to all players in the same game.
     *
     * @param session The WebSocket session that was just established.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String playerId = getPlayerIdFromSessionUri(session.getUri().getQuery());
        String gameId = getGameIdFromSessionUri(session.getUri().getQuery());
        playerSessions.put(playerId, session);

        Map<String, Object> broadcastJson = new HashMap<>();
        broadcastJson.put("gameId", gameId);
        broadcastJson.put("playerId", -1);
        broadcastJson.put("message", String.format("Player %s join the chat", playerId));
        String broadcastMessage = objectMapper.writeValueAsString(broadcastJson);
        broadcast(broadcastMessage, gameId);
    }

    /**
     * This method is called when a new WebSocket message is received.
     * It parses the incoming message as a JSON object, constructs a new broadcast message as a JSON object, and sends it to all players in the same game.
     *
     * @param session The WebSocket session that received the message.
     * @param message The text message that was received.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String playerId = getPlayerIdFromSessionUri(session.getUri().getQuery());
        String gameId = getGameIdFromSessionUri(session.getUri().getQuery());

        // Parse the message as JSON
        Map<String, Object> messageJson = objectMapper.readValue(payload, HashMap.class);
        int id = (int) messageJson.get("playerId");
        String text = messageJson.get("message").toString();

        // Broadcast the message as a JSON object
        Map<String, Object> broadcastJson = new HashMap<>();
        broadcastJson.put("gameId", gameId);
        broadcastJson.put("playerId", id);
        broadcastJson.put("message", text);
        String broadcastMessage = objectMapper.writeValueAsString(broadcastJson);
        broadcast(broadcastMessage, gameId);
    }

    /**
     * This method is called when an existing WebSocket connection is closed.
     * It removes the player's WebSocket session from the playerSessions map and sends a broadcast message to all players in the same game.
     *
     * @param session The WebSocket session that was just closed.
     * @param status  The reason why the WebSocket session was closed.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String playerId = getPlayerIdFromSessionUri(session.getUri().getQuery());
        String gameId = getGameIdFromSessionUri(session.getUri().getQuery());
        playerSessions.remove(playerId);
        Map<String, Object> broadcastJson = new HashMap<>();
        broadcastJson.put("gameId", gameId);
        broadcastJson.put("playerId", -1);
        broadcastJson.put("message", String.format("Player %s left the chat", playerId));
        String broadcastMessage = objectMapper.writeValueAsString(broadcastJson);
        broadcast(broadcastMessage, gameId);
    }

    /**
     * This method extracts the player ID from the query string of a WebSocket session URI.
     * The query string is in the format "playerId=<id>".
     *
     * @param uriQuery The query string of a WebSocket session URI.
     * @return The player ID extracted from the query string.
     */
    private String getPlayerIdFromSessionUri(String uriQuery) {
        return uriQuery.split("&")[0].split("=")[1];
    }

    /**
     * This method extracts the game ID from the path string of a WebSocket session URI.
     * The path string is in the format "/<gameID>/chat".
     *
     * @param uriPath The path string of a WebSocket session URI.
     * @return The game ID extracted from the path string.
     */

    private String getGameIdFromSessionUri(String uriQuery) {
        return uriQuery.split("=")[2];
    }

    /**
     * This method broadcasts a message to all players in the same game.
     *
     * @param message The message to broadcast.
     * @param gameId  The ID of the game for which to broadcast the message.
     */
    private void broadcast(String message, String gameId) throws Exception {
        for (WebSocketSession session : playerSessions.values()) {
            String sessionGameId = getGameIdFromSessionUri(session.getUri().getQuery());
            if (sessionGameId.equals(gameId)) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}
