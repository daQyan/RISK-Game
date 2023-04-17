package ece651.RISC.Server.Model;

import ece651.RISC.Server.MapFactory;
import ece651.RISC.shared.AttackAction;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.MapController;
import ece651.RISC.shared.MoveAction;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import ece651.RISC.shared.Territory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class Game {
    private int playerSize;
    private int initialTerritorySize;
    private ArrayList<Player> players;
    private GameMap myMap;
    private MapController myMapController;
    private Status.gameStatus myStatus;
    private OnlineServer2Client server2Client = new OnlineServer2Client();
    private Set<Integer> allocatedPlayerId = new HashSet<>();
    private Set<Player> allocatedPlayer = new HashSet<>();
    private int resourceGrow;


    private int playerInitUnits;
    private Round round;

    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private ArrayList<AttackAction> attackActions = new ArrayList<>();
    private Set<Integer> operatedPlayerId = new HashSet<>();

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

    // TODO: handle different player numbers
    public void init(int playerSize, int initialTerritorySize, int playerInitUnits){
        this.initialTerritorySize = initialTerritorySize;
        this.playerInitUnits = playerInitUnits;
        this.playerSize = playerSize;
        this.players = new ArrayList<>();
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(3);
        System.out.println("size:" + myMap.getMapSize());
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
        this.myMapController= new MapController(myMap);
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
        for(int i = playerIndex * initialTerritorySize; i < (playerIndex + 1 ) * initialTerritorySize; i++) {
            Territory t = myMap.getTerritory(i);
            player.addTerritories(t);
            t.setOwner(player);
        }
        players.add(player);
        if(players.size() == playerSize) {
            myStatus = Status.gameStatus.WAITINGPLAYERALLOCATE;
        }
        return playerIndex;
    }

    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    public void AllocateUnitFromPlayer(int playerId, Set<Integer> territoriesId, int numUnits) {
        for(Integer territoryId: territoriesId) {
            Territory serverSideTerritory = myMap.getTerritory(territoryId);
            if(serverSideTerritory.getOwner().getId() == playerId){
                serverSideTerritory.setNumUnits(numUnits);
            }
        }
        allocatedPlayerId.add(playerId);
        if(allocatedPlayerId.size() == playerSize) {
            myStatus = Status.gameStatus.PLAYING;
        }
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



//    public void letPlayerPlay() {
//        for(Player player: players){
//            server2Client.sendOneTurn(player, myMap, player.getStatus());
//            this.round = new Round(players, myMap, server2Client);
//        }
//    }


    public int getOperatedPlayerSize() {
        return operatedPlayerId.size();
    }

    public void resetOperatedPlayerId() {
        operatedPlayerId.clear();
    }
    public void recieveAction(Player player, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        System.out.println("playerOneTurn" + moveActions.size() +","+ attackActions.size());
        int operatedPlayerNum = round.playerOneTurn(player, moveActions, attackActions);
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
