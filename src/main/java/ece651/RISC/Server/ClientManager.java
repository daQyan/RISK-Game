package ece651.RISC.Server;

import ece651.RISC.shared.Player;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class ClientManager {
    private static final ConcurrentHashMap<String, Player> playersMap = new ConcurrentHashMap<>();

    public static void addPlayer(String name, Player player) {
        LOGGER.info("add player {}", name);
        playersMap.put(name, player);
    }

    public static int getSize() {
        return playersMap.size();
    }


    public static Collection<Player> getValues() {
        LOGGER.info("get webSocket connection values");
        return playersMap.values();
    }
}
