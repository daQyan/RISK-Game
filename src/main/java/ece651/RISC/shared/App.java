package ece651.RISC.shared;

import ece651.RISC.Server.Player;
import ece651.RISC.Server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {


    private Player p1;
    private Player p2;
    private Player p3;
    private Server server;


    public void main(String[] args) throws IOException {

        System.out.println("GAME START");
        initPlayers(p1, p2, p3);
        // initMap
        // sendTerritory to 3 p
        // each player placeUNit()
        // let server update map

        // play one turn until   Game.playOneTurn()
        // ....
        // one player win


    }

    public void initPlayers(Player p1, Player p2, Player p3) throws IOException {
        List<Player> playerList = new ArrayList<>();
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        for (int i = 0; i < 3; ++i) {
            playerList.get(i).makeUpPlayer(i);

        }


    }

}
