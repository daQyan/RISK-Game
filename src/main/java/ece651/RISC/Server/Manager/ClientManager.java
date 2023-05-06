package ece651.RISC.Server.Manager;

import ece651.RISC.shared.Player;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
/**
 * This class manages the connected WebSocket clients
 * It keeps track of the connected players and allows for adding and getting them
 */

@Slf4j
public class ClientManager {
    private static final ConcurrentHashMap<String, Player> playersMap = new ConcurrentHashMap<>();

    /**
     * Adds a player to the map of connected players.
     *
     * @param name   the name of the player
     * @param player the player object
     */
    public static void addPlayer(String name, Player player) {
        LOGGER.info("add player {}", name);
        playersMap.put(name, player);
    }

    /**
     * Returns the number of players currently connected.
     *
     * @return the number of players
     */
    public static int getSize() {
        return playersMap.size();
    }

    /**
     * Returns a collection of all the connected players.
     *
     * @return the collection of players
     */

    public static Collection<Player> getValues() {
        LOGGER.info("get webSocket connection values");
        return playersMap.values();
    }
}
