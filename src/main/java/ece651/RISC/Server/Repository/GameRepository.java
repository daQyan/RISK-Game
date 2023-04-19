package ece651.RISC.Server.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

import ece651.RISC.Server.Service.Game;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {
    private final Map<Long, Game> games;
    private long gameIdCounter;

    public GameRepository() {
        this.games = new LinkedHashMap<>();
        this.gameIdCounter = 0;
    }

    public synchronized long createGame(int roomSize, int initUnitsNum) {
        long gameId = gameIdCounter++;
        Game game = new Game(roomSize, initUnitsNum, gameId);
        games.put(gameId, game);
        return gameId;
    }

    public Game getGameById(long gameId) {
        return games.get(gameId);
    }

    public Map<Long, Game> getAllGames() {
        return games;
    }
}