package ece651.RISC.Client;

import ece651.RISC.shared.Action;

import java.util.ArrayList;

public class Client {
    private Player player;

    Client(Player player){
        this.player = player;
    }

    public void connectServer(){
        // TODO
    }

    public void initialize(){
        // TODO
    }

    public void oneRound() {
        ArrayList<Action> actions = new ArrayList<>();
        player.oneRound(actions);
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
