package ece651.RISC.shared;

import java.util.*;

public class GameMap {
    private ArrayList<Territory> Areas;

    public GameMap(ArrayList<Territory> areas) {
        System.out.println("Game map:" +  areas.size());
        this.Areas = areas;
    }

    public ArrayList<Territory> getAreas() {
        return Areas;
    }

    //for every turn, update the accessible territories for all the territory
    public void updateAccessible(){
        for(Territory t : Areas){
            //clear the old accessible areas
            t.setAccessibles(new ArrayList<>());
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
    public int getMapSize(){
        return Areas.size();
    }

    public ArrayList<Territory> getAllAreas(){
        return this.Areas;
    }

    public void setArea(int id, Territory t){
        Areas.set(id, t);
    }

    public Territory getArea(int id) {
        return Areas.get(id);
    }
}
