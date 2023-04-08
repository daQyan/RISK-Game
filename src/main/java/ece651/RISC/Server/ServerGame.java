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

    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private ArrayList<AttackAction> attackActions = new ArrayList<>();

    private Set<Player> operatedPlayers = new HashSet<>();

    public ServerGame() {
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
        this.myMapController= new MapController(myMap);
    }

    public ServerGame(int playerSize, int initialTerritorySize, Server2Client server2Client){
        this.initialTerritorySize = initialTerritorySize;
        this.playerSize = playerSize;
        this.players = new ArrayList<>();
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(3);
        this.myMapController= new MapController(myMap);
        this.server2Client = server2Client;
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
    }

    public int addPlayer(Player player) throws IOException {
        int playerIndex = players.size();
        player.setId(playerIndex);
        for(int i = playerIndex * initialTerritorySize; i <  (playerIndex + 1 ) * initialTerritorySize; i++) {
            Territory t = myMap.getArea(i);
            player.addTerritories(t);
            t.setOwner(player);
        }
        players.add(player);
        if(players.size() == playerSize) {
            myStatus = Status.gameStatus.WAITINGPLAYERALLOCATE;
            letPlayerAllocate();
        }
        return playerIndex;
    }

    public void letPlayerAllocate() throws IOException {
        for(Player player: players){
            server2Client.sendMap(player, myMap);
        }
    }

    public void playerAllocate(Player player, ArrayList<Territory> territories) throws IOException {
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

    public void letPlayerPlay() throws IOException {
        for(Player player: players){
            server2Client.sendMap(player, myMap);
        }
    }

    /**
     * do all the move action in the list
     */
    public void executeMoves(ArrayList<MoveAction> moveActions) {
        for (MoveAction move: moveActions) {
            move.moveTerritory();
        }
    }

    public void executeAttacks(ArrayList<AttackAction> attackActions) {
        Random rand = new Random();
        while(attackActions.size() > 0){
            int order = rand.nextInt(attackActions.size());
            String result = attackActions.get(order).attackTerritory();
            attackActions.remove(order);
        }
    }
    public void checkStatus(){
        for(Player sp : players){
            if(sp.getTerritories().isEmpty()){
                sp.setStatus(Status.playerStatus.LOSE);
            }
            if(sp.getTerritories().size() == myMap.getMapSize()){
                sp.setStatus(Status.playerStatus.WIN);
                this.myStatus = Status.gameStatus.FINISHED;
            }
        }
    }

    public void playerOneTurn(Player player, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) throws IOException {
        this.moveActions.addAll(moveActions);
        this.attackActions.addAll(attackActions);
        this.operatedPlayers.add(player);
        if(operatedPlayers.size() == playerSize) {
            playOneTurn(this.moveActions, this.attackActions);
            this.moveActions = new ArrayList<>();
            this.attackActions = new ArrayList<>();
            this.operatedPlayers = new HashSet<>();
        }
    }
    //play one turn of the game
    public void playOneTurn(ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions) throws IOException {
        executeMoves(moveActions);
        executeAttacks(attackActions);
        checkStatus();
        if(myStatus == Status.gameStatus.FINISHED) {
            // 通知所有player
        }
        //send information to players for networked game
        for(Player player: players) {
            server2Client.sendMap(player, myMap);
        }
    }





}
