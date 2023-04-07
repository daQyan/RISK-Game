package ece651.RISC.shared;

import java.util.*;

public class GameMap {
    private Set<Territory> Areas;

    public GameMap(Set<Territory> areas) {
        Areas = areas;
    }

    //for every turn, update the accessible territories for all the territory
    public void updateAccessible(){
        for(Territory t : Areas){
            Player p = t.getOwner();
            ArrayList<Territory> visited = new ArrayList<>();
            visited.add(t);
            Queue<Territory> next = new LinkedList<>();
            for(Territory n : t.getAdjacents()){
                if(n.getOwner().equals(p)){
                    next.add(n);
                }
            }
            searchAccessible(p, t, next, visited);
        }
    }

    //helper function to search every accessible territory
    public void searchAccessible(Player owner, Territory t, Queue<Territory> next, ArrayList<Territory> visited){
        if(next.isEmpty()){
            return;
        }
        while(!next.isEmpty()){
            Territory temp = next.peek();
            if(!visited.contains(temp)){
                visited.add(temp);
                t.addAccessible(temp);
                if(temp.getOwner().equals(owner)){
                    for(Territory i : temp.getAdjacents()){
                        if(i.getOwner().equals(owner)) {
                            next.add(i);
                        }
                    }
                }
            }
            next.poll();
        }
    }
    public void naturalIncrease(){
        for(Territory t : Areas){
            t.updateUnits(1);
        }
    }
    public int getMapSize(){
        return Areas.size();
    }

}
