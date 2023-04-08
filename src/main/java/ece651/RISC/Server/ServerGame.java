package ece651.RISC.Server;

import ece651.RISC.shared.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class ServerGame {
    private int playerSize;
    private int initialTerritorySize;
    private ArrayList<Player> players;
    private GameMap myMap;
    private MapController myMapController;
    private Status.gameStatus myStatus;
    private Server2Client server2Client;
    private Set<Player> allocatedPlayer = new HashSet<>();
    private int playerInitUnits;
    private Round round;

    public ServerGame(int playerSize, int initialTerritorySize, int playerInitUnits, Server2Client server2Client){
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

    public ServerGame() {
        this(3, 3, 30, null);
    }

    public int addPlayer(Player player) throws IOException {
        int playerIndex = players.size();
        player.setId(playerIndex);
        for(int i = playerIndex * initialTerritorySize; i <  (playerIndex + 1 ) * initialTerritorySize; i++) {
            Territory t = myMap.getArea(i);
            player.addTerriories(t);
            t.setOwner(player);
        }
        players.add(player);
        if(players.size() == playerSize) {
            myStatus = Status.gameStatus.WAITINGPLAYERALLOCATE;
            letPlayerAllocate();
        }
        return playerIndex;
    }

    public void letPlayerAllocate() {
        for(Player player: players){
            server2Client.sendAllocation(player, players, myMap, playerInitUnits);
        }
    }

    public void playerAllocate(Player player, ArrayList<Territory> territories) {
        for(Territory territory: territories) {
            Territory serverSideTerritory = myMap.getArea(territory.getId());
            if(serverSideTerritory.getOwner().equals(player)){
                serverSideTerritory.setNumUnits(territory.getNumUnits());
            }
        }
        allocatedPlayer.add(player);
        if(allocatedPlayer.size() == playerSize) {
            myStatus = Status.gameStatus.PLAYING;
            letPlayerPlay();
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
}
