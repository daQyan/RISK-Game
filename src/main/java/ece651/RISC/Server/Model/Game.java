package ece651.RISC.Server.Model;

import ece651.RISC.Online.OnlineServer2Client;
import ece651.RISC.Server.MapFactory;
import ece651.RISC.Server.Round;
import ece651.RISC.shared.AttackAction;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.MapController;
import ece651.RISC.shared.MoveAction;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Server2Client;
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
    private Server2Client server2Client;
    private Set<Integer> allocatedPlayerId = new HashSet<>();
    private int playerInitUnits;
    private Round round;

    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private ArrayList<AttackAction> attackActions = new ArrayList<>();
    private Set<Player> operatedPlayers = new HashSet<>();

    public Game() {
        this(3, 3, 30, null);
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(3);
        this.myMapController = new MapController(myMap);
        this.server2Client = new OnlineServer2Client();;

        System.out.println("ServerGame Constructor");
    }

    public Game(int playerSize, int initialTerritorySize, int playerInitUnits, Server2Client server2Client){
        this.initialTerritorySize = initialTerritorySize;
        this.playerSize = playerSize;
        this.playerInitUnits = playerInitUnits;

        this.players = new ArrayList<>();
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(3);
        this.myMapController = new MapController(myMap);
        this.server2Client = server2Client;
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
    }

    // TODO: handle different player numbers
    public void init(int playerSize, int initialTerritorySize, int playerInitUnits, Server2Client server2Client){
        this.initialTerritorySize = initialTerritorySize;
        this.playerInitUnits = playerInitUnits;
        this.playerSize = playerSize;
        this.players = new ArrayList<>();
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(3);
        System.out.println("size:" + myMap.getMapSize());
        this.server2Client = server2Client;
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
        this.myMapController= new MapController(myMap);
    }

    public int addPlayer(Player player) {
        int playerIndex = players.size();
        if(playerIndex > playerSize - 1) {
            return -1; // TODO: HANDLE HTTP ERRORs
        }
        player.setId(playerIndex);
        for(int i = playerIndex * initialTerritorySize; i < (playerIndex + 1 ) * initialTerritorySize; i++) {
            Territory t = myMap.getArea(i);
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
            Territory serverSideTerritory = myMap.getArea(territoryId);
            if(serverSideTerritory.getOwner().getId() == playerId){
                serverSideTerritory.setNumUnits(numUnits);
            }
        }
        allocatedPlayerId.add(playerId);
        if(allocatedPlayerId.size() == playerSize) {
            myStatus = Status.gameStatus.PLAYING;
        }
    }

    public void letPlayerPlay() {
        for(Player player: players){
            server2Client.sendOneTurn(player, myMap, player.getStatus());
            this.round = new Round(players, myMap, server2Client);
        }
    }

    public void playerOneTurn(Player player, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        int operatedPlayerNum = round.playerOneTurn(player, moveActions, attackActions);
        if(operatedPlayerNum == playerSize){
            playOneTurn();
        }
    }
    //play one turn of the game
    public void playOneTurn() {
        myStatus  = round.playOneTurn();
        if(myStatus == Status.gameStatus.FINISHED) {
            // 通知所有player
            return;
        }
        this.round = new Round(players, myMap, server2Client);
        //send information to players for networked game
        for(Player player: players) {
            server2Client.sendOneTurn(player, myMap, player.getStatus());
        }
    }

    public Status.gameStatus getStatus() {
        return myStatus;
    }
}
