package ece651.RISC.shared;

import java.util.*;

public class GameMap {
    private final ArrayList<Territory> territories;

    public GameMap(ArrayList<Territory> territories) {
        System.out.println("Game map:" +  territories.size());
        this.territories = territories;
    }

    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    //for every turn, update the accessible territories for all the territory
    public void updateAccessible(){
        for(Territory t : territories){
            //clear the old accessible territories
            t.setAccessibles(new LinkedHashMap<>());
            Player p = t.getOwner();
            ArrayList<Territory> visited = new ArrayList<>();
            visited.add(t);
            Queue<Territory> next = new LinkedList<>();
            for(Territory n : t.getAdjacents()){
                if(n.getOwner().equals(p)){
                    next.add(n);
                }
            }
            int cost = 2;
            searchAccessible(p, t, next, visited, cost);
        }

    }

    public void setAccessibleIdsFromAccessible() {
        for (Territory terr : territories) {
            terr.setAccessibleIdsFromItsAccessible(terr.getAccessibles());
        }
    }

    public void setAdjacentIdsFromAdjacent () {
        for (Territory terr : territories) {
            terr.setAdjacentIdsFromItsAdjacent(terr.getAdjacents());
        }
    }

    //helper function to search every accessible territory
    public void searchAccessible(Player owner, Territory t, Queue<Territory> next, ArrayList<Territory> visited, int cost){
        if(next.isEmpty()){
            return;
        }
        while(!next.isEmpty()){
            int size = next.size();
            while(size > 0){
                Territory temp = next.peek();
                if(!visited.contains(temp)){
                    visited.add(temp);
                    t.addAccessible(temp, cost);
                    if(temp.getOwner().equals(owner)){
                        for(Territory i : temp.getAdjacents()){
                            if(i.getOwner().equals(owner)) {
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
