package ece651.RISC.Server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SocketManager {
    //should implement sockets here
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public static void add(String key, WebSocketSession webSocketSession) {
        LOGGER.info("add webSocket connection {} ", key);
        sessions.put(key, webSocketSession);
    }

    public static void remove(String key) {
        LOGGER.info("remove webSocket connection {} ", key);
        sessions.remove(key);
    }

    public static Collection<WebSocketSession> getValues() {
        LOGGER.info("get webSocket connection values");
        return sessions.values();
    }
}
