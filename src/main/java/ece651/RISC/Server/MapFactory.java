package ece651.RISC.Server;

import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Territory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MapFactory {
    public GameMap createMap(int numPlayers){
        ArrayList<Territory> myTerritories = new ArrayList<>();
        Territory Narnia = new Territory(0, "Narnia");
        myTerritories.add(Narnia);
        Territory Midkemia = new Territory(1, "Midkemia");
        myTerritories.add(Midkemia);
        Territory Oz = new Territory(2, "Oz");
        myTerritories.add(Oz);
        Territory Gondor = new Territory(3, "Gondor");
        myTerritories.add(Gondor);
        Territory Mordor = new Territory(4, "Mordor");
        myTerritories.add(Mordor);
        Territory Hogwarts = new Territory(5, "Hogwarts");
        myTerritories.add(Hogwarts);
        Territory Elantris = new Territory(6, "Elantris");
        myTerritories.add(Elantris);
        Territory Scadrial = new Territory(7, "Scadrial");
        myTerritories.add(Scadrial);

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
            Territory Roshar = new Territory(8, "Roshar");
            myTerritories.add(Roshar);

            Roshar.addAdjacent(Elantris);
            Roshar.addAdjacent(Scadrial);
            Roshar.addAdjacent(Hogwarts);

            Hogwarts.addAdjacent(Roshar);
            Elantris.addAdjacent(Roshar);
            Scadrial.addAdjacent(Roshar);

            return new GameMap(myTerritories);
        }
        //10 territories
        Territory Hamilton = new Territory(9, "Hamilton");

        Hamilton.addAdjacent(Mordor);
        Hamilton.addAdjacent(Hogwarts);

        return new GameMap(myTerritories);
    }

}
