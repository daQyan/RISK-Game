package ece651.RISC.Client;

import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Territory;

import java.util.*;

public class MapTextView {
    private GameMap myMap;

    public MapTextView(GameMap myMap) {
        this.myMap = myMap;
    }

    public LinkedHashMap<Player, ArrayList<Territory>> sortTerritory(){
        LinkedHashMap<Player, ArrayList<Territory>> sorted = new LinkedHashMap<>();
        for(Territory t: myMap.getTerritories()){
            if(!sorted.containsKey(t.getOwner())){
                sorted.put(t.getOwner(), new ArrayList<>());
            }
            sorted.get(t.getOwner()).add(t);
        }
        return sorted;
    }

    public void displayMap(){
        LinkedHashMap<Player, ArrayList<Territory>> sorted = sortTerritory();
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
                result.append(")\n");
            }
            result.append("\n");
        }
        System.out.print(result.toString());
    }
}
