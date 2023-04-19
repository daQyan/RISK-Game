package ece651.RISC.Server.Service;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private String name;
    private Map<Integer, Game> games;

    public Room(String name) {
        this.name = name;
        this.games = new HashMap<Integer, Game>();
    }

    public String getName() {
        return name;
    }

    public void addGame(Game game) {
        games.put(game.getGameId(), game);
    }

    public Game getGame(int gameId) {
        return games.get(gameId);
    }

    public void removeGame(int gameId) {
        games.remove(gameId);
    }

    public int getNumberOfGames() {
        return games.size();
    }
}