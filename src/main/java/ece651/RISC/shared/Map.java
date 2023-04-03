package ece651.RISC.shared;

import ece651.RISC.Server.Player;

import java.util.*;

public class Map {
    private Set<Territory> Areas;

    public Map(Set<Territory> areas) {
        Areas = areas;
    }

    //only for 3 players
    private Set<Territory> createTerritory(){
        Set<Territory> myTerritories = new HashSet<>();
        Territory Narnia = new Territory("Narnia", null, 0, null, null);
        Territory Midkemia = new Territory("Midkemia", null, 0, null, null);
        Territory Oz = new Territory("Oz", null, 0, null, null);
        Territory Gondor = new Territory("Gondor", null, 0, null, null);
        Territory Mordor = new Territory("Mordor", null, 0, null, null);
        Territory Hogwarts = new Territory("Hogwarts", null, 0, null, null);
        Territory Elantris = new Territory("Elantris", null, 0, null, null);
        Territory Scadrial = new Territory("Scadrial", null, 0, null, null);
        Territory Roshar = new Territory("Roshar", null, 0, null, null);

        Narnia.addAdjacent(Midkemia);
        Narnia.addAdjacent(Elantris);

        Midkemia.addAdjacent(Narnia);
        Midkemia.addAdjacent(Oz);
        Midkemia.addAdjacent(Scadrial);

        Oz.addAdjacent(Midkemia);
        Oz.addAdjacent(Gondor);
        Oz.addAdjacent(Mordor);
        Oz.addAdjacent(Scadrial);

        Gondor.addAdjacent(Oz);
        Gondor.addAdjacent(Mordor);

        Mordor.addAdjacent(Oz);
        Mordor.addAdjacent(Gondor);
        Mordor.addAdjacent(Scadrial);
        Mordor.addAdjacent(Hogwarts);

        Hogwarts.addAdjacent(Mordor);
        Hogwarts.addAdjacent(Scadrial);
        Hogwarts.addAdjacent(Roshar);

        Elantris.addAdjacent(Narnia);
        Elantris.addAdjacent(Midkemia);
        Elantris.addAdjacent(Scadrial);
        Elantris.addAdjacent(Roshar);

        Scadrial.addAdjacent(Elantris);
        Scadrial.addAdjacent(Midkemia);
        Scadrial.addAdjacent(Oz);
        Scadrial.addAdjacent(Mordor);
        Scadrial.addAdjacent(Hogwarts);
        Scadrial.addAdjacent(Roshar);

        Roshar.addAdjacent(Elantris);
        Roshar.addAdjacent(Scadrial);
        Roshar.addAdjacent(Hogwarts);

        return myTerritories;
    }
    //default: 3 player, each player with 3 territories
    public Map(int numPlayers){
        if(numPlayers == 3){
            this.Areas = createTerritory();
        }
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
