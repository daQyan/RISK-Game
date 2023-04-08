package ece651.RISC.Client;

import ece651.RISC.shared.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClientPlayer extends Player {

    private BufferedReader inputReader;
    private PrintStream out;

    private GameMap map;
    private int initUnits;

    public void setInitUnits(int initUnits) {
        this.initUnits = initUnits;
    }

    private Client2Server communicator;

    public void setCommunicator(Client2Server communicator) {
        this.communicator = communicator;
    }
// TODO: refresh moves and attacks

    public ClientPlayer(String name, BufferedReader inputReader, PrintStream out){
        super(name);
        this.inputReader = inputReader;
        this.out = out;
    }

    Territory getTerritoryByName(String terName) {
        for (Territory t : map.getAreas()) {
            if (terName.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    public void move(ArrayList<MoveAction> moveActions) throws IOException {
        String welcome = "Player, " + this.name + "you can move units within your adjacent territories:" + getMyTerritoryName();
        out.println(welcome);
        String sourceTer = "Please specify the source territory with the territory name: ";
        out.println(sourceTer);
        String sourceName = inputReader.readLine();
        String targetTer = "Please specify the target territory with the territory name: ";
        out.println(targetTer);
        String targetName = inputReader.readLine();
        String unitNum = "Please specify the number of units to move";
        out.println(unitNum);
        int unitMove = Integer.parseInt(inputReader.readLine());

        MoveAction move = new MoveAction(getTerritoryByName(sourceName), getTerritoryByName(targetName), unitMove, Status.actionStatus.MOVE, this);
        // TODO
        // Add check
        // check if available
        // if available, move out
        moveActions.add(move);
    }

    public void attack(ArrayList<AttackAction> attackActions) throws  IOException {
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

        AttackAction attack = new AttackAction(getTerritoryByName(sourceName), getTerritoryByName(targetName), unitMove, Status.actionStatus.MOVE, this);
        attackActions.add(attack);
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
//    public void makeUpPlayer(int i) throws IOException {
//        out.println("Please give yourself a name: ");
//        String myName = inputReader.readLine();
//        // TODO: check name valid
//        this.name = myName;
//        this.id = i;
//    }

    public void initUnitPlacement(){
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
    private void parseUnitsPlacement(String prompt, Set<Territory> myTerritory) throws IOException {
        List numList = new ArrayList();
        String[] parts = prompt.split(" ");
        int sumUnits = 0;
        for (int i = 0; i <=3; ++i) {
            int num = Integer.parseInt(parts[i]);
            numList.add(i);
            sumUnits += num;
        }
        // check Unit Input is valid and update the territory units
        if (sumUnits == initUnits) {
            int index = 0;
            for (Territory t : myTerritory) {
                t.updateUnits((Integer) numList.get(index));
            }
            communicator.sendAllocation(new ArrayList<Territory>(myTerritory));
        }
        else {
            throw new IllegalArgumentException("Total input units beyond scope!\n");
        }
    }


    // player play one turn with move and attack orders
    public void playOneTurn() throws IOException {
        ArrayList<MoveAction> moveActions = new ArrayList<>();
        ArrayList<AttackAction> attackActions = new ArrayList<>();
        out.println("options: M for move, A for attack, D for Done");
        // keep receiving order input until (D)one
        while (true) {
            String s = inputReader.readLine();
            if (s.equals("D")) {}
            switch (s) {
                case "D":
                    communicator.sendActions(moveActions, attackActions);
                    break;
                case "M":
                    move(moveActions);
                    break;
                case"A":
                    attack(attackActions);
                    break;
                default:
                    out.println("Invalid input, please input again");
            }
        }
    }

    public void connectServer() throws IOException {
        communicator.sendName(this);
    }
}
