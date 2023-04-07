package ece651.RISC.Server;

import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Territory;

import java.util.HashSet;
import java.util.Set;

public class MapFactory {
    public GameMap createMap(int numPlayers){
        Set<Territory> myTerritories = new HashSet<>();
        Territory Narnia = new Territory(0, "Narnia", 0, null, null, null);
        Territory Midkemia = new Territory(1, "Midkemia", 0, null, null, null);
        Territory Oz = new Territory(2, "Oz", 0, null, null, null);
        Territory Gondor = new Territory(3, "Gondor", 0, null, null, null);
        Territory Mordor = new Territory(4, "Mordor", 0, null, null, null);
        Territory Hogwarts = new Territory(5, "Hogwarts", 0, null, null, null);
        Territory Elantris = new Territory(6, "Elantris", 0, null, null, null);
        Territory Scadrial = new Territory(7, "Scadrial", 0, null, null, null);

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

        Elantris.addAdjacent(Narnia);
        Elantris.addAdjacent(Midkemia);
        Elantris.addAdjacent(Scadrial);

        Scadrial.addAdjacent(Elantris);
        Scadrial.addAdjacent(Midkemia);
        Scadrial.addAdjacent(Oz);
        Scadrial.addAdjacent(Mordor);
        Scadrial.addAdjacent(Hogwarts);

        //8 territories
        if(numPlayers == 2 || numPlayers == 4){
            return new GameMap(myTerritories);
        }
        //9 territories
        if(numPlayers == 3){
            Territory Roshar = new Territory(8, "Roshar", 0, null, null, null);

            Roshar.addAdjacent(Elantris);
            Roshar.addAdjacent(Scadrial);
            Roshar.addAdjacent(Hogwarts);

            Hogwarts.addAdjacent(Roshar);
            Elantris.addAdjacent(Roshar);
            Scadrial.addAdjacent(Roshar);

            return new GameMap(myTerritories);
        }
        //10 territories
        Territory Hamilton = new Territory(9, "Hamilton", 0, null, null, null);

        Hamilton.addAdjacent(Mordor);
        Hamilton.addAdjacent(Hogwarts);

        return new GameMap(myTerritories);
    }

}
