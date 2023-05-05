package ece651.RISC.shared;

import java.util.*;
/**
 * This class represents the game map that contains all the territories in the game.
 * It has methods to update the accessible territories for each territory, set the accessible IDs from the accessible territories,
 * set the adjacent IDs from the adjacent territories, and search every accessible territory.
 */
public class GameMap {
    private final ArrayList<Territory> territories;

    /**
     * Constructs a game map with the given list of territories.
     *
     * @param territories the list of territories to be included in the game map
     */
    public GameMap(ArrayList<Territory> territories) {
        System.out.println("Game map:" + territories.size());
        this.territories = territories;
    }

    /**
     * Returns the list of territories in the game map.
     *
     * @return the list of territories in the game map
     */
    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    /**
     * Updates the accessible territories for all the territories in the game map.
     * This method is called at the beginning of every turn.
     */
    public void updateAccessible() {
        for (Territory t : territories) {
            //clear the old accessible territories
            t.setAccessibles(new LinkedHashMap<>());
            Player p = t.getOwner();
            Player ally = t.getAllyOwner();
            ArrayList<Territory> visited = new ArrayList<>();
            visited.add(t);
            Queue<Territory> next = new LinkedList<>();
            for (Territory n : t.getAdjacents()) {
                if (n.getOwner().equals(p)) {
                    next.add(n);
                } else if (n.getAllyOwner() != null && n.getAllyOwner().equals(ally)) {
                    next.add(n);
                }
            }
            int cost = 2;
            searchAccessible(p, ally, t, next, visited, cost);
        }

    }

    /**
     * Returns the number of players in the game.
     *
     * @return the number of players in the game
     */
    public int getNumPlayers() {
        Set<Player> players = new HashSet<>();
        for (Territory territory : territories) {
            players.add(territory.getOwner());
        }
        return players.size();
    }

    /**
     * Sets the accessible IDs for each territory from its accessible territories.
     */
    public void setAccessibleIdsFromAccessible() {
        for (Territory terr : territories) {
            terr.setAccessibleIdsFromItsAccessible(terr.getAccessibles());
        }
    }

    /**
     * Sets the adjacent IDs for each territory from its adjacent territories.
     */
    public void setAdjacentIdsFromAdjacent() {
        for (Territory terr : territories) {
            terr.setAdjacentIdsFromItsAdjacent(terr.getAdjacents());
        }
    }

    /**
     * Searches every accessible territory starting from the given territory.
     *
     * @param owner   the player that owns the territory
     * @param ally    the player that owns the ally territory
     * @param t       the starting territory
     * @param next    a queue of the next territories to be visited
     * @param visited a list of visited territories
     * @param cost    the cost to move from one territory to its neighbor
     */
    public void searchAccessible(Player owner, Player ally, Territory t, Queue<Territory> next, ArrayList<Territory> visited, int cost) {
        if (next.isEmpty()) {
            return;
        }
        while (!next.isEmpty()) {
            int size = next.size();
            while (size > 0) {
                Territory temp = next.peek();
                if (!visited.contains(temp)) {
                    visited.add(temp);
                    t.addAccessible(temp, cost);
                    if(temp.getOwner().equals(owner) || (temp.getAllyOwner() != null && temp.getAllyOwner().equals(ally))){
                        for(Territory i : temp.getAdjacents()){
                            if(i.getOwner().equals(owner) || (i.getAllyOwner() != null && i.getAllyOwner().equals(ally))) {
                                next.add(i);
                            }
                        }
                    }
                }
                next.poll();
                --size;
            }
            cost += 2;
        }
    }

    public int getMapSize(){
        return territories.size();
    }

    public void setTerritory(int id, Territory t){
        territories.set(id, t);
    }

    public Territory getTerritory(int id) {
        return territories.get(id);
    }
}
