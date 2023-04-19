package ece651.RISC.Server.Service;

import ece651.RISC.Server.MapFactory;
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
import java.util.Set;

@Data
@Component
public class Game {
    private int playerSize;
    private int initialTerritorySize;
    private ArrayList<Player> players;

    private HashMap<Integer, ArrayList<Integer>> territoriesOfPlayers;
    private GameMap myMap;
    private MapController myMapController;
    private Status.gameStatus myStatus;

    private OnlineServer2Client server2Client = new OnlineServer2Client();
    private Set<Integer> allocatedPlayerId = new HashSet<>();
    private Set<Player> allocatedPlayer = new HashSet<>();
    private long gameId;
    private int playerInitUnits;
    private Round round;

    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private ArrayList<AttackAction> attackActions = new ArrayList<>();

    private int operatedPlayerNum = 0;

    public Game() {
        this(3, 3, 30);
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(3);
        this.myMapController = new MapController(myMap);

        System.out.println("ServerGame Constructor");
    }

    public Game(int playerSize, int initialTerritorySize, int playerInitUnits){
        this.initialTerritorySize = initialTerritorySize;
        this.playerSize = playerSize;
        this.playerInitUnits = playerInitUnits;

        this.players = new ArrayList<>();
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(3);
        this.myMapController = new MapController(myMap);
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
    }

    public Game(int playerSize, int playerInitUnits, long gameId){
        this.playerSize = playerSize;
        this.playerInitUnits = playerInitUnits;
        this.setGameId(gameId);
        this.players = new ArrayList<>();
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(playerSize);
        this.myMapController = new MapController(myMap);
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
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
            this.round = new Round(players, myMap);
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
        this.round = new Round(players, myMap);
        return Status.gameStatus.PLAYING;
    }

    public Status.gameStatus getStatus() {
        return myStatus;
    }
}
