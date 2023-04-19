package ece651.RISC.Server.Service;

import ece651.RISC.Server.MapFactory;
import ece651.RISC.Server.Model.GamePlayer;
import ece651.RISC.shared.AttackAction;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.MapController;
import ece651.RISC.shared.MoveAction;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import ece651.RISC.shared.Territory;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Data
@Component
public class Game {
    private int playerSize;
    private int initialTerritorySize;
    private ArrayList<Player> players;
    private ArrayList<GamePlayer> gamePlayers;

    private HashMap<Integer, ArrayList<Integer>> territoriesOfPlayersMap;
    private GameMap myMap;
    private MapController myMapController;
    private Status.gameStatus myStatus;

    private OnlineServer2Client server2Client = new OnlineServer2Client();
    private Set<Integer> allocatedPlayerId = new HashSet<>();
    private Set<Player> allocatedPlayer = new HashSet<>();
    private long gameId;
    private int resourceGrow;
    private int playerInitUnits;
    private Round round;

    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private ArrayList<AttackAction> attackActions = new ArrayList<>();

    private int operatedPlayerNum = 0;

    public Game() {
        this(3, 3, 5, 30);
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(3);
        this.myMapController = new MapController(myMap);

        System.out.println("ServerGame Constructor");
    }

    public Game(int playerSize, int initialTerritorySize, int resourceGrow, int playerInitUnits){
        this.initialTerritorySize = initialTerritorySize;
        this.playerSize = playerSize;
        this.resourceGrow = resourceGrow;
        this.playerInitUnits = playerInitUnits;

        this.players = new ArrayList<>();
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(3);
        this.myMapController = new MapController(myMap);
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
    }

    // most important
    public Game(int playerSize, int playerInitUnits, long gameId){
        this.territoriesOfPlayersMap = new LinkedHashMap<>();
        this.playerSize = playerSize;
        this.playerInitUnits = playerInitUnits;
        this.setGameId(gameId);
        this.gamePlayers = new ArrayList<>();
        this.players = new ArrayList<>();
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(playerSize);
        this.myMapController = new MapController(myMap);
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
    }
    public void initTerritoriesOfPlayersMap(int eachBlocksOfTerritory) {
            for(int i = 0; i < playerSize; i++) {
                territoriesOfPlayersMap.put(i, new ArrayList<>());
                for(int j = eachBlocksOfTerritory * i; j < eachBlocksOfTerritory * (i + 1); j++) {
                    territoriesOfPlayersMap.get(i).add(j);
                }
            }

        for(Integer a:territoriesOfPlayersMap.keySet()) {
            System.out.println(a + ": ");
            for(Integer b: territoriesOfPlayersMap.get(a)) {
                System.out.print(b + " ");
            }
            System.out.println();
        }
    }
    public long getGameId() {
        return gameId;
    }
    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public GameMap getMyMap() {
        return myMap;
    }

    public int addPlayer(Player player) {
        int playerIndex = players.size();
        if(playerIndex > playerSize - 1) {
            return -1; // TODO: HANDLE HTTP ERRORs
        }
        player.setId(playerIndex);

        players.add(player);
        if(players.size() == playerSize) {
            // allocate the territories
            for(int i = playerIndex * initialTerritorySize; i < (playerIndex + 1 ) * initialTerritorySize; i++) {
                Territory t = myMap.getTerritory(i);
                player.addTerritories(t);
                t.setOwner(player);
            }
            myStatus = Status.gameStatus.WAITINGPLAYERALLOCATE;
        }
        return playerIndex;
    }


    public Boolean tryAddPlayer(long userId, String username) {
        GamePlayer gmPlayer = new GamePlayer(userId, username);
        int playerIndex = gamePlayers.size();
        if(playerIndex > playerSize - 1) {
            return false;
        }
        gmPlayer.setPlayerIndex(playerIndex);
        gamePlayers.add(gmPlayer);

        if(gamePlayers.size() == playerSize) {
            // allocate the territories
            switch (playerSize) {
                case 2:
                    initTerritoriesOfPlayersMap(4);
                    break;
                case 3:
                    initTerritoriesOfPlayersMap(3);
                    break;
                case 4:
                case 5:
                    initTerritoriesOfPlayersMap(2);
                    break;
            }
            myStatus = Status.gameStatus.WAITINGPLAYERALLOCATE;
        }
        return true;
    }

    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    public void playerAllocate(Player player, ArrayList<Territory> territories) {
        for(Territory territory: territories) {
            Territory serverSideTerritory = myMap.getTerritory(territory.getId());
            if(serverSideTerritory.getOwner().equals(player)){
                serverSideTerritory.setNumUnits(territory.getNumUnits());
                System.out.println(serverSideTerritory.getName() + ":" + serverSideTerritory.getNumUnits() + "," + territory.getNumUnits());
            }
        }
        allocatedPlayer.add(player);
        if(allocatedPlayer.size() == playerSize) {
            myStatus = Status.gameStatus.PLAYING;
            myMap.updateAccessible();
            System.out.println("Game can begin");
            this.round = new Round(players, myMap, resourceGrow);
        }
    }
    public int getAllocatedPlayerSize() {
        return allocatedPlayer.size();
    }

    public void setOperatedPlayerNum(int num) {
        operatedPlayerNum = num;
    }

    public int getOperatedPlayerNum() {
        return operatedPlayerNum;
    }

    public void handleActions(Player player, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        System.out.println("playerOneTurn" + moveActions.size() +","+ attackActions.size());
        int operatedPlayerNum = round.playerOneTurn(player, moveActions, attackActions);
        setOperatedPlayerNum(operatedPlayerNum);
        if(operatedPlayerNum == playerSize){
            playOneTurn();
        }
        // TODO: what kinds of information should return
    }

    //play one turn of the game
    public Status.gameStatus playOneTurn() {
        myStatus  = round.playOneTurn();
        myMap.updateAccessible();
        if(myStatus == Status.gameStatus.FINISHED) {
            return Status.gameStatus.FINISHED;
        }
        this.round = new Round(players, myMap, resourceGrow);
        return Status.gameStatus.PLAYING;
    }

    public Status.gameStatus getStatus() {
        return myStatus;
    }
}
