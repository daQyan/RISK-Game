package ece651.RISC.Server.Repository;

import ece651.RISC.Server.Service.Game;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents the repository for Game objects. It provides methods for
 * creating new games and retrieving games by their IDs.
 */
@Repository
public class GameRepository {
    private final Map<Long, Game> games;
    private long gameIdCounter;

    /**
     * Constructor for creating a new GameRepository object.
     */
    public GameRepository() {
        this.games = new ConcurrentHashMap<>();
        this.gameIdCounter = 0;
    }

    /**
     * Creates a new game with the specified room size and initial number of units.
     *
     * @param roomSize     the number of players in the game
     * @param initUnitsNum the initial number of units for each player
     * @return the ID of the new game
     */
    public synchronized long createGame(int roomSize, int initUnitsNum) {
        long gameId = gameIdCounter++;
        Game game = new Game(roomSize, initUnitsNum, gameId);
        games.put(gameId, game);
        return gameId;
    }

    /**
     * Returns the game with the specified ID.
     *
     * @param gameId the ID of the game to retrieve
     * @return the game with the specified ID
     */
    public Game getGameById(long gameId) {
        return games.get(gameId);
    }

    /**
     * Returns a map of all games in the repository.
     *
     * @return a map of all games in the repository
     */
    public Map<Long, Game> getAllGames() {
        return games;
    }
}