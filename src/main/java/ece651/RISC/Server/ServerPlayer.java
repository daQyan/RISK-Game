package ece651.RISC.Server;

import ece651.RISC.Status;
import ece651.RISC.shared.Action;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Territory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Set;

public class ServerPlayer extends Player {
    //should implement socket here
    private Status.playerStatus myStatus;
    private ArrayList<MoveAction> myMoveOrder;
    private ArrayList<AttackAction> myAttackOrder;

    private BufferedReader inputReader;
    private PrintStream out;


    public ServerPlayer(int id, String name, Set<Territory> myTerritory) {
        super(id, name, myTerritory);
        this.myStatus = Status.playerStatus.PLAYING;
    }

    public void changeStatus(Status.playerStatus s){
        myStatus = s;
    }


    public void move(ArrayList<Action> actions) throws IOException {
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
    private String getMyTerritoryName() {
        String s = null;
        // TODO: init myTerritory first
        for (Territory t : getTerritories()) {
            s += t.getName();
        }
        return s;
    }

    // initialize the name and units for one player
    public void makeUpName() throws IOException {
        out.println("Please give yourself a name: ");
        String myName = inputReader.readLine();
        // TODO: check name valid
        // update name in Player ? or let server do this
        this.name = myName;
    }

    public void initUnitPlacement() throws IOException{
        out.println("Player, " + this.name + "you have in total 12 units and following territory, please specify the units for " +
                getMyTerritoryName() +
                "with the format <unit1> <unit2> <unit3>");
        String unitsPlacement = inputReader.readLine();
        //TODO: check input valid: <format>, <sum>
        parseUnitsPlacement(unitsPlacement, getTerritories());
    }

    // parse the input from user and update its Territories
    private void parseUnitsPlacement(String prompt, Set<Territory> myTerritory) {
        // TODO
    }


    // player play one turn with move and attack orders
    public void playOneTurn(ArrayList<Action> actions) throws IOException {
        out.println("options: M for move, A for attack, D for Done");
        // keep receiving order input until (D)one
        while (true) {
            String s = inputReader.readLine();
            if (s.equals("D")) break;
            switch (s) {
                case "M":
                    move(actions);
                    break;
                case"A":
                    attack(actions);
                    break;
                default:
                    out.println("Invalid input, please input again");
                    playOneTurn(actions);
            }
        }

    }


    public void parseMoveAction(String order){

    }
    // checkIsMyTerritory

}
