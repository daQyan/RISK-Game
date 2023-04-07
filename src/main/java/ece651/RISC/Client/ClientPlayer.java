package ece651.RISC.Client;

import ece651.RISC.shared.Action;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Territory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Set;

public class ClientPlayer extends Player {

    final BufferedReader inputReader;
    final PrintStream out;

    public ClientPlayer(int id, String name, Set<Territory> territories, BufferedReader inputReader, PrintStream out) {
        super(id, name, territories);
        this.inputReader = inputReader;
        this.out = out;
    }

    public void move(ArrayList<Action> actions) throws  IOException {
        // user input
        // find terriories
        // check if available
        // if available, move out
        // add action
    }

    public void attack(ArrayList<Action> actions) throws  IOException {
        // user input
        // find terriories
        // check if available
        // if available, move out
        // add action
    }

    public void doOneTurn(ArrayList<Action> actions) throws IOException {
        out.println("options: M for move, A for attack");
        String s = inputReader.readLine();
        switch (s) {
            case "M":
                move(actions);
                break;
            case"A":
                attack(actions);
                break;
            default:
                out.println("Invalid input, please input again");
                doOneTurn(actions);
        }
    }

    public String toJSON(){
        return name;
    }
}
