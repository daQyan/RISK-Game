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

    public void setMap(GameMap map) {
        this.map = map;
        this.view = new MapTextView(map);
    }

    private int initUnits;

    private Status.playerStatus status;

    private MapTextView view;

    @Override
    public void setStatus(Status.playerStatus status) {
        this.status = status;
    }

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
        this.status = Status.playerStatus.INIT;
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
        String welcome = "Player " + this.name + ", you can move units within your adjacent territories:" + getMyTerritoryName();
        out.println(welcome);
        String sourceTerMesg = "Please specify the source territory with the territory name: ";
        out.println(sourceTerMesg);
        String sourceName = inputReader.readLine();
        String targetTerMesg = "Please specify the target territory with the territory name: ";
        out.println(targetTerMesg);
        String targetName = inputReader.readLine();
        String unitNum = "Please specify the number of units to move";
        out.println(unitNum);
        int unitMove = Integer.parseInt(inputReader.readLine());
        Territory source = getTerritoryByName(sourceName);
        Territory target = getTerritoryByName(targetName);

        MoveAction move = new MoveAction(source, target, unitMove, Status.actionStatus.MOVE, this);
        ActionChecker checker = new ActionChecker();
        String checkResult = checker.checkMoveRule(this, source, target, unitMove);

        if (checkResult == null) moveActions.add(move);
        else throw new IllegalArgumentException(checkResult);
        // modify the map
        move.moveTerritory();
    }

    public void attack(ArrayList<AttackAction> attackActions) throws  IOException {
        String welcome = "Player " + this.name + ", which territory would you want to attack? " + getMyTerritoryName();
        String sourceTerMesg = "Please specify the source territory with the territory name: ";
        String targetTerMesg = "Please specify the target territory with the territory name: ";
        String unitNumMesg = "Please specify the number of units to move";
        out.println(welcome);
        String sourceTer, targetTer;
        Territory source = null;
        Territory target = null;
        int unitAttack = 0;
        while (true) {
            try {
                out.println(sourceTerMesg);
                sourceTer = inputReader.readLine();
                out.println(targetTerMesg);
                targetTer = inputReader.readLine();
                out.println(unitNumMesg);
                unitAttack = Integer.parseInt(inputReader.readLine());

                source = getTerritoryByName(sourceTer);
                target = getTerritoryByName(targetTer);

                ActionChecker checker = new ActionChecker();
                String checkResult = checker.checkAttackRule(this, source, target, unitAttack);
                if (checkResult != null) throw new IllegalArgumentException(checkResult);
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        AttackAction attack = new AttackAction(source, target, unitAttack, Status.actionStatus.ATTACK, this);
        attackActions.add(attack);
        // modify the map
        attack.attackTerritory();
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
    public void initializePlayer(int i) throws IOException {
        out.println("Please give yourself a name: ");
        String myName = inputReader.readLine();
        // TODO: check name valid
        this.name = myName;
        this.id = i;
    }

    public void initUnitPlacement(){
        this.view.displayMap();
        String prompt = "Player " + this.name + ", you have in total " + initUnits + " units and following territory, please specify the units for "
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
    private void parseUnitsPlacement(String prompt, ArrayList<Territory> myTerritory) throws IOException {
        ArrayList<Integer> numList = new ArrayList();
        String[] parts = prompt.split(" ");
        int sumUnits = 0;
        for (int i = 0; i <parts.length; ++i) {
            int num = Integer.parseInt(parts[i]);
            numList.add(num);
            sumUnits += num;
        }
        // check Unit Input is valid and update the territory units
        if (sumUnits == initUnits) {
            for (int i = 0; i < numList.size(); i++) {
                myTerritory.get(i).updateUnits(numList.get(i));
            }
            communicator.sendAllocation(myTerritory);
        }
        else {
            throw new IllegalArgumentException("Total input units beyond scope!\n");
        }
    }

    public void playFirstTurn(int playerID) throws IOException{
        initializePlayer(playerID);
        initUnitPlacement();
    }
    // player play one turn with move and attack orders
    public void playOneTurn()  {
        view.displayMap();
        ArrayList<MoveAction> moveActions = new ArrayList<>();
        ArrayList<AttackAction> attackActions = new ArrayList<>();
        out.println("options: M for move, A for attack, D for Done");
        // keep receiving order input until (D)one
        try{
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
        } catch(IOException error) {
            System.out.println(error.getMessage());
        }
    }

    public void connectServer() throws IOException {
        communicator.sendName(this);
    }
}
