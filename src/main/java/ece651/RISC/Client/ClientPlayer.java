package ece651.RISC.Client;

import ece651.RISC.shared.Action;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Territory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class ClientPlayer extends Player {
    final BufferedReader inputReader;
    final PrintStream out;

    public ClientPlayer(int id, String name, BufferedReader inputReader, PrintStream out) {
        super(id, name);
        this.inputReader = inputReader;
        this.out = out;
    }

    public ClientPlayer(int id, String name) {
        this(id, name, null, null);
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
}
