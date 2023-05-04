package ece651.RISC.Server.Service;

import ece651.RISC.Server.MapFactory;
import ece651.RISC.shared.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Data
@Component
@Slf4j
public class Game {
    private int playerSize;
    private int initialTerritorySize;
    private ArrayList<Player> players;
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
        this.players = new ArrayList<>();
        MapFactory mf = new MapFactory();
        this.myMap = mf.createMap(playerSize);
        this.myMapController = new MapController(myMap);
        this.myStatus = Status.gameStatus.WAITINGPLAYER;
    }
    public void initTerritoriesOfPlayersMap(int eachBlocksOfTerritory) {
        // for each player, add the territories to the player
        for(int i = 0; i < playerSize; i++) {
            Player player = players.get(i);
            for(int j = eachBlocksOfTerritory * i; j < eachBlocksOfTerritory * (i + 1); j++) {
                Territory t = myMap.getTerritory(j);
                t.setOwner(player);
                // set owner id of territory
                t.setOwnerId(player.getId());
            }
        }

        // print each territory of each player
        for(int i = 0; i < playerSize; i++) {
            System.out.println( "id: " + players.get(i).getId() + " name: " + players.get(i).getName());
            for(int j = 0; j < myMap.getTerritories().size(); j++) {
                if (myMap.getTerritory(j).getOwner().getId() == i) {
                    System.out.print(myMap.getTerritory(j).getId() + " ");
                }
            }
            System.out.println();
        }
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


    public String tryAddPlayer(int userId, String username) {
        // If there is one player with the same username, return false
        for(Player p: players) {
            if(p.getName().equals(username)) {
                return "You've already joined the game!";
            }
        }
        // If the game is full, return false
        if(players.size() >= playerSize) {
            return "The game is full!";
        }

        Player player = new Player(userId, username);
        players.add(player);

        if(players.size() == playerSize) {
            // allocate the territories
            switch (playerSize) {
                case 2:
                    initTerritoriesOfPlayersMap(5);
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
            LOGGER.info("game has enough player, they can allocate");
        }
        return null;
    }

    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    public void playerAllocate(Player player, ArrayList<Territory> territories) {
        System.out.println("The territory is allocated to the player: " + player.getId());

        for(Territory territory: territories) {
            Territory serverSideTerritory = myMap.getTerritory(territory.getId());
            if(serverSideTerritory.getOwner().equals(player)){
                serverSideTerritory.setNumUnits(territory.getNumUnits());
                serverSideTerritory.updateMyUnits(0,territory.getNumUnits());
                System.out.println(serverSideTerritory.getName() + ":" + serverSideTerritory.getNumUnits() + "," + territory.getNumUnits());
            }
        }
        allocatedPlayer.add(player);
        if(allocatedPlayer.size() == playerSize) {
            myStatus = Status.gameStatus.PLAYING;
            // set adjacent ids from adjacent territories
            myMap.setAdjacentIdsFromAdjacent();

            // set accessible territories and accessible ids for each territory
            myMap.updateAccessible();
            myMap.setAccessibleIdsFromAccessible();

            LOGGER.info("Allocation end, Game can begin");
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

    public void handleActions(Player player, ArrayList<MoveAction> moveActions, ArrayList<AttackAction> attackActions, ArrayList<UpgradeTechAction> upgradeTechActions, ArrayList<UpgradeUnitAction> upgradeUnitActions) {
        // parse actions
        ArrayList<MoveAction> newMoves = parseMoves(moveActions);
        ArrayList<AttackAction> newAttacks = parseAtk(attackActions);
        ArrayList<UpgradeUnitAction> newUpgradedUnitActions = parseUpgradeUnit(upgradeUnitActions);
        // print the move actions and attack actions
        System.out.println("Move actions:");
        for(MoveAction moveAction: newMoves) {
            System.out.println(moveAction.getSourceTerritory().getName() + " -> " + moveAction.getTargetTerritory().getName() + " " + moveAction.getHitUnits());
        }
        System.out.println("Attack actions:");
        for(AttackAction attackAction: newAttacks) {
            System.out.println(attackAction.getSourceTerritory().getName() + " -> " + attackAction.getTargetTerritory().getName() + " " + attackAction.getHitUnits());
        }
        // print the upgrade actions
        System.out.println("Upgrade actions:");
        for(UpgradeUnitAction upgradeUnitAction: newUpgradedUnitActions) {
            System.out.println(upgradeUnitAction.getTerritory().getName() + " " + upgradeUnitAction.getOldType() + " -> " + upgradeUnitAction.getNewType() + " nums:" + upgradeUnitAction.getUnitNum());
        }

        int operatedPlayerNum = round.playerOneTurn(player, newMoves, newAttacks, upgradeTechActions, newUpgradedUnitActions);
        setOperatedPlayerNum(operatedPlayerNum);
        if(operatedPlayerNum == playerSize){
            playOneTurn();
        }
    }

    private ArrayList<UpgradeUnitAction> parseUpgradeUnit(ArrayList<UpgradeUnitAction> upgradeUnitActions) {
        for (UpgradeUnitAction upgradeUnitAct : upgradeUnitActions) {
            // replace each territory
            upgradeUnitAct.setTerritory(myMap.getTerritory(upgradeUnitAct.getTerritory().getId()));
        }
        return upgradeUnitActions;
    }

    private ArrayList<AttackAction> parseAtk(ArrayList<AttackAction> attackActions) {
        for (AttackAction atk : attackActions) {
            // replace each territory
            atk.setSourceTerritory(myMap.getTerritory(atk.getSourceTerritory().getId()));
            atk.setTargetTerritory(myMap.getTerritory(atk.getTargetTerritory().getId()));
        }
        return attackActions;
    }

    private ArrayList<MoveAction> parseMoves(ArrayList<MoveAction> moveActions) {
        for (MoveAction atk : moveActions) {
            // replace each territory
            atk.setSourceTerritory(myMap.getTerritory(atk.getSourceTerritory().getId()));
            atk.setTargetTerritory(myMap.getTerritory(atk.getTargetTerritory().getId()));
        }
        return moveActions;
    }

    //play one turn of the game
    public Status.gameStatus playOneTurn() {
        myStatus  = round.playOneTurn();
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
