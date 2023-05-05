package ece651.RISC.Client;

import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Territory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the MapTextView class that displays the game map in a textual format.
 * This class extends the MapView class, which provides the basic functionality for displaying the map.
 * The sortTerritory method sorts the territories on the map by owner, and returns a LinkedHashMap that maps each owner to their territories.
 * The displayMap method generates a string that displays the territories and units owned by each player, as well as the adjacent territories for each territory.
 */
public class MapTextView extends MapView {
    /**
     * The constructor calls the constructor of the parent MapView class.
     */
    public MapTextView(GameMap myMap) {
        super(myMap);
    }

    /**
     * This method sorts the territories on the map by owner and returns a LinkedHashMap that maps each owner to their territories.
     * It iterates through each territory on the map and adds it to the ArrayList associated with the territory's owner in the LinkedHashMap.
     * If the owner is not already a key in the LinkedHashMap, it adds a new key-value pair for the owner and an empty ArrayList.
     *
     * @return A LinkedHashMap that maps each owner to their territories.
     */
    private LinkedHashMap<Player, ArrayList<Territory>> sortTerritory() {
        LinkedHashMap<Player, ArrayList<Territory>> sorted = new LinkedHashMap<>();
        for (Territory t : myMap.getTerritories()) {
            if (!sorted.containsKey(t.getOwner())) {
                sorted.put(t.getOwner(), new ArrayList<>());
            }
            sorted.get(t.getOwner()).add(t);
        }
        return sorted;
    }

    /**
     * This method generates a string that displays the territories and units owned by each player, as well as the adjacent territories for each territory.
     * It first calls the sortTerritory method to obtain a LinkedHashMap that maps each owner to their territories.
     * It then iterates through the key-value pairs in the LinkedHashMap and generates a string that displays the owner's name, each territory they own, and the adjacent territories for each territory.
     * The string is printed to the console.
     */
    @Override
    public void displayMap() {
        LinkedHashMap<Player, ArrayList<Territory>> sorted = sortTerritory();
        StringBuilder result = new StringBuilder();
        String delimiter = "-------------\n";
        for (Map.Entry<Player, ArrayList<Territory>> entry : sorted.entrySet()) {
            result.append(entry.getKey().getName());
            result.append(":\n");
            result.append(delimiter);
            for (int i = 0; i < entry.getValue().size(); ++i) {
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
