package ece651.RISC.Client;

import java.util.ArrayList;

public class Player {
    private final String name;

    private ArrayList<Terriory> myTerriories;

    public Player(String name) {
        this.name = name;
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
}
