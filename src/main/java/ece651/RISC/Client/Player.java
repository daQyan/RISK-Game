package ece651.RISC.Client;

import ece651.RISC.shared.Action;
import ece651.RISC.shared.Map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class Player {
    private final String name;

    private Map myMap;

    public void setMyMap(Map myMap) {
        this.myMap = myMap;
    }

    private ArrayList<Terriory> myTerriories;

    final BufferedReader inputReader;
    final PrintStream out;

    public Player(String name, Map myMap, BufferedReader inputReader, PrintStream out) {
        this.name = name;
        this.myMap = myMap;
        this.inputReader = inputReader;
        this.out = out;
        this.myTerriories = new ArrayList<>();
    }

    public void addTerriories(Terriory t){
        myTerriories.add(t);
    }

    @Override
    public String toString() {
        String res = name + ":" + System.lineSeparator() +
                "-------------" + System.lineSeparator();
        for(Terriory t: myTerriories){
            res += (t.toString() + System.lineSeparator());
        }
        return res;
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
