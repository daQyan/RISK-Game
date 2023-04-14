package ece651.RISC.Server;

import ece651.RISC.shared.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ServerGame {
    private int playerSize;
    private int initialTerritorySize;
    private ArrayList<Player> players;
    private GameMap myMap;

    public GameMap getMyMap() {
        return myMap;
    }

    private MapController myMapController;
    private Status.gameStatus myStatus;
    private Server2Client server2Client;
    private Set<Player> allocatedPlayer = new HashSet<>();
    private int playerInitUnits;
    private Round round;

    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private ArrayList<AttackAction> attackActions = new ArrayList<>();
    private Set<Player> operatedPlayers = new HashSet<>();

    public ServerGame() {
        this(3, 3, 30, null);
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(3);
        this.myMapController = new MapController(myMap);

        System.out.println("ServerGame Constructor");
    }

    public ServerGame(int playerSize, int initialTerritorySize, int playerInitUnits, Server2Client server2Client){
        this.initialTerritorySize = initialTerritorySize;
        this.playerSize = playerSize;
        this.playerInitUnits = playerInitUnits;

        this.players = new ArrayList<>();
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(playerSize);
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
        this.server2Client = server2Client;
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
        this.myMapController= new MapController(myMap);
    }

    public void addPlayer(Player player) {
        int playerIndex = players.size();
        if(playerIndex > playerSize - 1) {
            return; // TODO: HANDLE HTTP ERRORs
        }
        player.setId(playerIndex);
        for(int i = playerIndex * initialTerritorySize; i < (playerIndex + 1 ) * initialTerritorySize; i++) {
            Territory t = myMap.getTerritory(i);
            player.addTerritories(t);
            t.setOwner(player);
        }
        players.add(player);
        server2Client.sendId(player, playerIndex);
        if(players.size() == playerSize) {
            myStatus = Status.gameStatus.WAITINGPLAYERALLOCATE;
            letPlayerAllocate();
        }
    }

    public void letPlayerAllocate() {
        System.out.println("letPlayerAllocate");
        for(Player player: players){
            server2Client.sendAllocation(player, players, myMap, playerInitUnits);
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
            letPlayerPlay();
        }
    }

    public void letPlayerPlay() {
        this.round = new Round(players, myMap, server2Client);
        for(Player player: players){
            server2Client.sendOneTurn(player, myMap, player.getStatus());
        }
    }

    public void playerOneTurn(Player player, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) {
        System.out.println("playerOneTurn" + moveActions.size() +","+ attackActions.size());
        int operatedPlayerNum = round.playerOneTurn(player, moveActions, attackActions);
        if(operatedPlayerNum == playerSize){
            playOneTurn();
        }
    }
    //play one turn of the game
    public void playOneTurn() {
        myStatus  = round.playOneTurn();
        myMap.updateAccessible();
        if(myStatus == Status.gameStatus.FINISHED) {
            // 通知所有player
            return ;
        }
        this.round = new Round(players, myMap, server2Client);
        //send information to players for networked game
        for(Player player: players) {
            server2Client.sendOneTurn(player, myMap, player.getStatus());
        }
    }
}
