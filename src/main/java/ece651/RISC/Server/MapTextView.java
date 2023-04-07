package ece651.RISC.Server;

import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Territory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MapTextView {
    private GameMap myMap;

    public ArrayList<HashMap<ServerPlayer, Territory>> sortTerritory(){
        Set<ServerPlayer> temp = new HashSet<>();
        ArrayList<HashMap<ServerPlayer, Territory>> sorted = new ArrayList<>();
        for(Territory t: myMap.getAllAreas()){
            if(!temp.contains(t.getOwner())){
                HashMap<ServerPlayer, Territory> sort = new HashMap<>();

            }
        }


    }

    public String displayMap(){
        ArrayList<ServerPlayer> players;
        for(Territory t: myMap.getAllAreas()){

        }
    }
}
