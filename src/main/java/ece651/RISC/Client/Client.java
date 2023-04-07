package ece651.RISC.Client;

import ece651.RISC.shared.Action;

import java.io.IOException;
import java.util.ArrayList;

public class Client {
    private ClientPlayer player;

    Client(ClientPlayer player){
        this.player = player;
    }

    public void connectServer(){
        // TODO
    }

    public void initialize(){
        // TODO
    }

    public void oneRound() throws IOException {
        ArrayList<Action> actions = new ArrayList<>();
        player.playOneTurn();
        // send actions to server
    }

    public void play(){
        while(true) {
            // receive board from server
            // reset player
            // oneRound
        }
    }
}
