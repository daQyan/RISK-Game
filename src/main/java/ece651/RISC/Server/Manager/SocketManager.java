package ece651.RISC.Server.Manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
/**
 * The SocketManager class manages the WebSocket sessions of the players.
 */
@Slf4j
public class SocketManager {
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * Adds a new WebSocket session to the map.
     *
     * @param key              A unique identifier for the session.
     * @param webSocketSession The WebSocketSession to be added.
     */
    public static void add(String key, WebSocketSession webSocketSession) {
        LOGGER.info("add webSocket connection {} ", key);
        sessions.put(key, webSocketSession);
    }

    /**
     * Removes a WebSocket session from the map.
     *
     * @param key The unique identifier of the session to be removed.
     */
    public static void remove(String key) {
        LOGGER.info("remove webSocket connection {} ", key);
        sessions.remove(key);
    }

    /**
     * Returns a Collection of all the WebSocketSessions in the map.
     *
     * @return A Collection of WebSocketSessions.
     */

    public static Collection<WebSocketSession> getValues() {
        LOGGER.info("get webSocket connection values");
        return sessions.values();
    }
}
