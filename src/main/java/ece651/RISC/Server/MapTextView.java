package ece651.RISC.Server;

import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Territory;

import java.util.*;

public class MapTextView {
    private GameMap myMap;

    public HashMap<Player, ArrayList<Territory>> sortTerritory(){
        HashMap<Player, ArrayList<Territory>> sorted = new HashMap<>();
        for(Territory t: myMap.getAllAreas()){
            if(!sorted.containsKey(t.getOwner())){
                sorted.put(t.getOwner(), new ArrayList<>());
            }
            sorted.get(t.getOwner()).add(t);
        }
        return sorted;
    }

    public String displayMap(){
        HashMap<Player, ArrayList<Territory>> sorted = sortTerritory();
        StringBuilder result = new StringBuilder();
        String delimiter = "-------------\n";
        for(Map.Entry<Player, ArrayList<Territory>> entry : sorted.entrySet() ){
            result.append(entry.getKey().getName());
            result.append(":\n");
            result.append(delimiter);
            for(int i = 0; i < entry.getValue().size(); ++i){
                result.append(entry.getValue().get(i).getNumUnits());
                result.append(" units in ");
                result.append(entry.getValue().get(i).getName());
                result.append(" (next to: ");
                for(int j = 0; j < entry.getValue().get(i).getAdjacents().size(); ++j){
                    if(j > 0){
                        result.append(", ");
                    }
                    result.append(entry.getValue().get(i).getAdjacents().get(j).getName());
                }
                result.append(")");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
