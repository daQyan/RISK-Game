package ece651.RISC.Offline;

import ece651.RISC.Client.ClientGame;
import ece651.RISC.Client.ClientPlayer;
import ece651.RISC.Server.ServerGame;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Server2Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class App {


    private Player p1;
    private Player p2;
    private Player p3;

    public void main(String[] args) throws IOException {

        System.out.println("GAME START");
        int playerSize = 3;
        int initialTerritorySize = 3;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader((System.in)));
        PrintStream out = System.out;
        ClientPlayer clientPlayer1 = new ClientPlayer("player1", inputReader, out);
        ClientPlayer clientPlayer2 = new ClientPlayer("player2", inputReader, out);
        ClientPlayer clientPlayer3 = new ClientPlayer("player3", inputReader, out);
        ClientGame clientGame1 = new ClientGame(clientPlayer1);
        ClientGame clientGame2 = new ClientGame(clientPlayer2);
        ClientGame clientGame3 = new ClientGame(clientPlayer3);
        ArrayList<ClientGame> clientGames = new ArrayList<>();
        clientGames.add(clientGame1);
        clientGames.add(clientGame2);
        clientGames.add(clientGame3);
        OfflineServer2Client server2Client = new OfflineServer2Client(clientGames);
        ServerGame serverGame = new ServerGame(3, 3, server2Client);
        OfflineClient2Server client2Server1 = new OfflineClient2Server(serverGame,clientPlayer1);
        clientPlayer1.setCommunicator(client2Server1);
        OfflineClient2Server client2Server2 = new OfflineClient2Server(serverGame,clientPlayer2);
        clientPlayer2.setCommunicator(client2Server2);
        OfflineClient2Server client2Server3 = new OfflineClient2Server(serverGame,clientPlayer3);
        clientPlayer3.setCommunicator(client2Server3);

        clientPlayer1.connectServer();
        clientPlayer2.connectServer();
        clientPlayer3.connectServer();
    }

}
