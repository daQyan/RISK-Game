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
        Territory Roshar = new Territory(8, "Roshar");

        Territory Hamilton = new Territory(9, "Hamilton");

        if(numPlayers == 2 || numPlayers == 5){
            myTerritories.add(Roshar);
            myTerritories.add(Hamilton);

            Narnia.addAdjacent(Hogwarts);
            Narnia.addAdjacent(Elantris);
            Narnia.addAdjacent(Midkemia);
            Narnia.addAdjacent(Oz);

            Midkemia.addAdjacent(Narnia);
            Midkemia.addAdjacent(Oz);
            Midkemia.addAdjacent(Scadrial);
            Midkemia.addAdjacent(Hogwarts);
            Midkemia.addAdjacent(Gondor);

            Oz.addAdjacent(Midkemia);
            Oz.addAdjacent(Narnia);
            Oz.addAdjacent(Elantris);
            Oz.addAdjacent(Mordor);
            Oz.addAdjacent(Hamilton);

            Gondor.addAdjacent(Midkemia);
            Gondor.addAdjacent(Mordor);
            Gondor.addAdjacent(Scadrial);
            Gondor.addAdjacent(Roshar);

            Mordor.addAdjacent(Oz);
            Mordor.addAdjacent(Gondor);
            Mordor.addAdjacent(Roshar);
            Mordor.addAdjacent(Hamilton);

            Hogwarts.addAdjacent(Midkemia);
            Hogwarts.addAdjacent(Scadrial);
            Hogwarts.addAdjacent(Elantris);
            Hogwarts.addAdjacent(Narnia);

            Elantris.addAdjacent(Narnia);
            Elantris.addAdjacent(Oz);
            Elantris.addAdjacent(Hamilton);
            Elantris.addAdjacent(Hogwarts);

            Scadrial.addAdjacent(Roshar);
            Scadrial.addAdjacent(Midkemia);
            Scadrial.addAdjacent(Gondor);
            Scadrial.addAdjacent(Hogwarts);

            Roshar.addAdjacent(Gondor);
            Roshar.addAdjacent(Mordor);
            Roshar.addAdjacent(Hamilton);
            Roshar.addAdjacent(Scadrial);


            Hamilton.addAdjacent(Mordor);
            Hamilton.addAdjacent(Oz);
            Hamilton.addAdjacent(Elantris);
            Hamilton.addAdjacent(Roshar);
            //10 territories
            return new GameMap(myTerritories);
        }
        //9 territories
        if(numPlayers == 3){
            myTerritories.add(Roshar);

            Narnia.addAdjacent(Hogwarts);
            Narnia.addAdjacent(Elantris);
            Narnia.addAdjacent(Midkemia);
            Narnia.addAdjacent(Oz);

            Midkemia.addAdjacent(Narnia);
            Midkemia.addAdjacent(Oz);
            Midkemia.addAdjacent(Scadrial);
            Midkemia.addAdjacent(Hogwarts);
            Midkemia.addAdjacent(Gondor);

            Oz.addAdjacent(Midkemia);
            Oz.addAdjacent(Narnia);
            Oz.addAdjacent(Elantris);
            Oz.addAdjacent(Mordor);
            Oz.addAdjacent(Roshar);

            Gondor.addAdjacent(Midkemia);
            Gondor.addAdjacent(Mordor);
            Gondor.addAdjacent(Scadrial);

            Mordor.addAdjacent(Oz);
            Mordor.addAdjacent(Gondor);
            Mordor.addAdjacent(Roshar);

            Hogwarts.addAdjacent(Midkemia);
            Hogwarts.addAdjacent(Scadrial);
            Hogwarts.addAdjacent(Elantris);
            Hogwarts.addAdjacent(Narnia);

            Elantris.addAdjacent(Narnia);
            Elantris.addAdjacent(Oz);
            Elantris.addAdjacent(Roshar);
            Elantris.addAdjacent(Hogwarts);

            Scadrial.addAdjacent(Roshar);
            Scadrial.addAdjacent(Midkemia);
            Scadrial.addAdjacent(Gondor);
            Scadrial.addAdjacent(Hogwarts);

            Roshar.addAdjacent(Gondor);
            Roshar.addAdjacent(Elantris);
            Roshar.addAdjacent(Mordor);
            Roshar.addAdjacent(Scadrial);
            return new GameMap(myTerritories);
        }
        ////for 4 players, 8 territories
        else{
            Narnia.addAdjacent(Hogwarts);
            Narnia.addAdjacent(Mordor);
            Narnia.addAdjacent(Midkemia);
            Narnia.addAdjacent(Oz);

            Midkemia.addAdjacent(Narnia);
            Midkemia.addAdjacent(Oz);
            Midkemia.addAdjacent(Mordor);
            Midkemia.addAdjacent(Elantris);
            Midkemia.addAdjacent(Gondor);

            Oz.addAdjacent(Midkemia);
            Oz.addAdjacent(Narnia);
            Oz.addAdjacent(Hogwarts);
            Oz.addAdjacent(Gondor);
            Oz.addAdjacent(Scadrial);

            Gondor.addAdjacent(Midkemia);
            Gondor.addAdjacent(Oz);
            Gondor.addAdjacent(Elantris);
            Gondor.addAdjacent(Scadrial);

            Mordor.addAdjacent(Narnia);
            Mordor.addAdjacent(Elantris);
            Mordor.addAdjacent(Midkemia);
            Mordor.addAdjacent(Hogwarts);

            Hogwarts.addAdjacent(Mordor);
            Hogwarts.addAdjacent(Scadrial);
            Hogwarts.addAdjacent(Oz);
            Hogwarts.addAdjacent(Narnia);

            Elantris.addAdjacent(Mordor);
            Elantris.addAdjacent(Midkemia);
            Elantris.addAdjacent(Gondor);
            Elantris.addAdjacent(Scadrial);

            Scadrial.addAdjacent(Oz);
            Scadrial.addAdjacent(Elantris);
            Scadrial.addAdjacent(Gondor);
            Scadrial.addAdjacent(Hogwarts);

            return new GameMap(myTerritories);
        }
        //do not support players more than 5 or less than 2
    }
}
