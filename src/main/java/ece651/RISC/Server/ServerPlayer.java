package ece651.RISC.Server;

import ece651.RISC.Status;
import ece651.RISC.shared.Action;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Territory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServerPlayer extends Player {
    //should implement socket here
    private Status.playerStatus myStatus;
    private ArrayList<MoveAction> myMoveOrder;
    private ArrayList<AttackAction> myAttackOrder;

    private BufferedReader inputReader;
    private PrintStream out;
    private int maxUnits;

    // TODO: refresh moves and attacks

    public ServerPlayer(int id, String name, Set<Territory> myTerritory) {
        super(id, name, myTerritory, null);
        this.myStatus = Status.playerStatus.PLAYING;
    }

    public void changeStatus(Status.playerStatus s){
        myStatus = s;
    }

    Territory getNameFromMap(String terName) {
        for (Territory t : map.getAreas()) {
            if (terName.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    public void move(ArrayList<Action> actions) throws IOException {
        String welcome = "Player, " + this.name + "Where would you want to move in your territories: " + getMyTerritoryName();
        String sourceTer = "Please specify the source territory with the territory name: ";
        String targetTer = "Please specify the target territory with the territory name: ";
        String unitNum = "Please specify the number of units to move";
        out.println(welcome);
        String sourceName, targetName;
        int unitMove;
        while (true) {
            try {
                sourceName = inputReader.readLine();
                //checkSource(sourceName);
                targetName = inputReader.readLine();
                //checkAccess(targetName);
                unitMove = Integer.parseInt(inputReader.readLine());
                //checkUnits(unitMove);
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        MoveAction move = new MoveAction(getNameFromMap(sourceName), getNameFromMap(targetName), unitMove, Status.actionStatus.MOVE, this);
        myMoveOrder.add(move);
        // user input
        // find territories
        // check if available
        // if available, move out
        // add action
    }

    public void attack(ArrayList<Action> actions) throws  IOException {
        String welcome = "Player, " + this.name + "Which territory would you want to attack? " + getMyTerritoryName();
        String sourceTer = "Please specify the source territory with the territory name: ";
        String targetTer = "Please specify the target territory with the territory name: ";
        String unitNum = "Please specify the number of units to move";
        out.println(welcome);
        String sourceName, targetName;
        int unitMove;
        while (true) {
            try {
                sourceName = inputReader.readLine();
                //checkSource(sourceName);
                targetName = inputReader.readLine();
                //checkAccess(targetName);
                unitMove = Integer.parseInt(inputReader.readLine());
                //checkUnits(unitMove);
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        AttackAction attack = new AttackAction(getNameFromMap(sourceName), getNameFromMap(targetName), unitMove, Status.actionStatus.MOVE, this);
        myAttackOrder.add(attack);
    }

    // must init myTerritory first before call this
    private String getMyTerritoryName() {
        StringBuilder s = new StringBuilder();
        String sep = "";
        for (Territory t : getTerritories()) {
            s.append(sep);
            s.append(t.getName());
            sep = ", ";
        }
        s.append("\n");
        return s.toString();
    }

    // initialize the name and units for one player
    // player_id = i
    public void makeUpPlayer(int i) throws IOException {
        out.println("Please give yourself a name: ");
        String myName = inputReader.readLine();
        // TODO: check name valid
        this.name = myName;
        this.id = i;
    }

    public void initUnitPlacement() throws IOException{
        String prompt = "Player, " + this.name + "you have in total 12 units and following territory, please specify the units for "
                + getMyTerritoryName() + "with the format <unit1> <unit2> <unit3>";
        out.println(prompt);

        String unitsInput;
        while (true) {
            try {
                unitsInput = inputReader.readLine();
                // follow the format <int><space><int><space><int><space>
                if (! unitsInput.matches("^\\d+\\s+\\d+\\s+\\d+$")) {
                    throw new IllegalArgumentException("Invalid input format! format: <unit1> <unit2> <unit3>\n");
                }
                parseUnitsPlacement(unitsInput, getTerritories());
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // parse the input from user and update its Territories
    private void parseUnitsPlacement(String prompt, Set<Territory> myTerritory) {
        List numList = new ArrayList();
        String[] parts = prompt.split(" ");
        int sumUnits = 0;
        for (int i = 0; i <=3; ++i) {
            int num = Integer.parseInt(parts[i]);
            numList.add(i);
            sumUnits += num;
        }
        // check Unit Input is valid and update the territory units
        if (sumUnits <= maxUnits) {
            int index = 0;
            for (Territory t : myTerritory) {
                t.updateUnits((Integer) numList.get(index));
            }
        }
        else {
            throw new IllegalArgumentException("Total input units beyond scope!\n");
        }
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
