package ece651.RISC.Client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import ece651.RISC.shared.ActionChecker;
import ece651.RISC.shared.AttackAction;
import ece651.RISC.shared.Client2Server;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.MoveAction;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import ece651.RISC.shared.Territory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class RestClientPlayer extends Player {

    private BufferedReader inputReader;
    private PrintStream out;

    public GameMap getMap() {
        return map;
    }

    private GameMap map;

    public void setMap(GameMap map) {
        this.map = map;
        this.view = new MapTextView(map);
        setTerritories(new ArrayList<>());
        for(Territory t: map.getTerritories()){
            if(this.equals(t.getOwner())) {
                addTerritories(t);
            }
        }
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

    @JSONField(serialize = false, deserialize = false)
    public ArrayList<MoveAction> moveActions = new ArrayList<>();

    @JSONField(serialize = false, deserialize = false)
    public ArrayList<AttackAction> attackActions = new ArrayList<>();

    public RestClientPlayer(String name, BufferedReader inputReader, PrintStream out){
        super(name);
        this.inputReader = inputReader;
        this.out = out;
        this.status = Status.playerStatus.INIT;
    }

    public RestClientPlayer(BufferedReader inputReader, PrintStream out){
        super();
        this.inputReader = inputReader;
        this.out = out;
        this.status = Status.playerStatus.INIT;
    }

    public void readPlayerName() {
        try{
            while (true) {
                out.println("WELCOME TO THE GAME. Please give yourself a name: ");

                String myName = inputReader.readLine();
                this.setName(myName);
                break;
            }
        } catch(IOException error) {
            System.out.println(error.getMessage());
        }
    }

    public Territory getTerritoryByName(String terName) {
        if (terName == null) return null;
        for (Territory t : map.getTerritories()) {
            if (terName.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    private Territory checkSource(ActionChecker checker, String sourceTerMesg) throws IOException {
        Territory source = null;
        while (true) {
            try {
                out.println(sourceTerMesg);
                String sourceTer = inputReader.readLine();
                source = getTerritoryByName(sourceTer);
                String result = checker.checkSource(this, source, "move");
                if (result != null) {
                    throw new IllegalArgumentException(result);
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return source;
    }

    private Territory checkTarget(ActionChecker checker, String targetTerMesg, Territory source, Status.actionStatus status) throws IOException {
        Territory target = null;
        while (true) {
            try {
                out.println(targetTerMesg);
                String targetTer = inputReader.readLine();
                target = getTerritoryByName(targetTer);
                String result = null;
                if (status == Status.actionStatus.MOVE) {
                    result = checker.checkAccess(source, target);
                }
                else result = checker.checkAtkTarget(source, target);
                if (result != null) {
                    throw new IllegalArgumentException(result);
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return target;
    }


    private int checkUnits(ActionChecker checker, String unitNumMesg, Territory sourceTerritory, String type) throws IOException {
        int units = 0;
        while (true) {
            try {
                out.println(unitNumMesg);
                units = Integer.parseInt(inputReader.readLine());
                String result = checker.checkUnits(sourceTerritory, units, type);
                if (result != null) {
                    throw new IllegalArgumentException(result);
                }
                break;
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return units;
    }


    public void move() throws IOException {
        String welcome = "Player " + this.name + ", you can move units within your accessible territories: " + getMyTerritoryName();
        String sourceTerMesg = "Please specify the source territory with the territory name: ";
        String targetTerMesg = "Please specify the target territory with the territory name: ";
        String unitNumMesg = "Please specify the number of units to move";
        out.println(welcome);

        Territory source = null;
        Territory target = null;
        int unitMove = 0;

        ActionChecker checker = new ActionChecker();
        source = checkSource(checker, sourceTerMesg);
        target = checkTarget(checker, targetTerMesg, source, Status.actionStatus.MOVE);
        unitMove = checkUnits(checker, unitNumMesg, source, "move");

        MoveAction move = new MoveAction(source, target, unitMove, Status.actionStatus.MOVE, this);
        move.moveOut(); // check done here
        moveActions.add(move);

        out.println("Move Successful ~");
        System.out.println("**-------------------------------------------------------------------------------------**");
    }

    public void attack() throws  IOException {
        String welcome = "Player " + this.name + ", which territory would you want to attack? " + getMyTerritoryName();
        String sourceTerMesg = "Please specify the source territory with the territory name: ";
        String targetTerMesg = "Please specify the target territory with the territory name: ";
        String unitNumMesg = "Please specify the number of units use to attack";
        out.println(welcome);
        Territory source = null;
        Territory target = null;
        int unitAttack = 0;

        ActionChecker checker = new ActionChecker();
        source = checkSource(checker, sourceTerMesg);
        target = checkTarget(checker, targetTerMesg, source, Status.actionStatus.ATTACK);
        unitAttack = checkUnits(checker, unitNumMesg, source, "attack");

        AttackAction attack = new AttackAction(source, target, unitAttack, Status.actionStatus.ATTACK, this);
        attack.moveOut(); // check done here
        attackActions.add(attack);

        out.println("Attack Successful ~");
        System.out.println("**-------------------------------------------------------------------------------------**");
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

    public void displayMap() {
        this.view.displayMap();
    }

    public ArrayList<Territory> initUnitPlacement() {
        displayMap();
        int numTer = this.territories.size();
        String prompt = "Hi~ Player " + this.name + ", you have in total " + initUnits + " units and " + numTer + " territory, please specify the units for "
                + getMyTerritoryName() + "with the format: <unit1> <unit2> ... <unitN>, where N is the number of your territory. ";
        String unitsInput;
        out.println(prompt);
        ArrayList<Territory> terList = new ArrayList<>();
        while (true) {
            try {
                unitsInput = inputReader.readLine();
                switch (numTer) {
                    case 2:
                        if (! unitsInput.matches("^\\d+\\s+\\d+$")) {
                            throw new IllegalArgumentException("Invalid input format! format: <unit1> <unit2> \n");
                        }
                        break;
                    case 3:
                        if (! unitsInput.matches("^\\d+\\s+\\d+\\s+\\d+$")) {
                            throw new IllegalArgumentException("Invalid input format! format: <unit1> <unit2> <unit3>\n");
                        }
                        break;
                    case 4:
                        if (! unitsInput.matches("^\\d+\\s+\\d+\\s+\\d+\\s+\\d+$")) {
                            throw new IllegalArgumentException("Invalid input format! format: <unit1> <unit2> <unit3> <unit4> <unit5>\n");
                        }
                        break;
                }
                System.out.println("initUnitPlacement" + getTerritories().size());
                terList = parseUnitsPlacement(unitsInput);
                break;
            } catch (IllegalArgumentException | IOException e) {
                System.out.println("initUnitPlacement" + e.getStackTrace());
                System.out.println(e.getMessage());
            }
        }
        out.println("Unit Placement Success!");
        System.out.println("**-------------------------------------------------------------------------------------**");
        return terList;
    }

    // parse the input from user and update its Territories
    private ArrayList<Territory> parseUnitsPlacement(String prompt) {
        ArrayList<Territory> myTerritory = getTerritories();
        ArrayList<Integer> numList = new ArrayList();
        String[] parts = prompt.split(" ");
        int sumUnits = 0;
        for (int i = 0; i < parts.length; ++i) {
            int num = Integer.parseInt(parts[i]);
            numList.add(num);
            sumUnits += num;
        }
        // check Unit Input is valid and update the territory units
        if (sumUnits == initUnits) {
            for (int i = 0; i < numList.size(); i++) {
                myTerritory.get(i).updateUnits(numList.get(i));
            }
        }
        else {
            throw new IllegalArgumentException("Total input units beyond or below scope!\n");
        }

        return myTerritory;
    }

    // player play one turn with move and attack orders
    public void readActions()  {
        moveActions.clear();
        attackActions.clear();
        if (checkLose()) return;
        if (checkWin()) return;
        // keep receiving order input until (D)one
        try{
            while (true) {
//                view.displayMap();
                out.println(name + ", your options: M for move, A for attack, D for Done");
                String s = inputReader.readLine().toUpperCase();
                if (s.equals("D")) {
                    System.out.println("D" + moveActions.size());
                    break;
                }
                switch (s) {
                    case "M":
                        move();
                        break;
                    case "A":
                        attack();
                        break;
                    default:
                        out.println("Invalid input, please input again");
                }
            }
        } catch(IOException error) {
            System.out.println(error.getMessage());
        }
    }

    public ArrayList<MoveAction> getMoveActions() {
        return moveActions;
    }

    public ArrayList<AttackAction> getAttackActions() {
        return attackActions;
    }

    private boolean checkLose() {
        if (status == Status.playerStatus.LOSE) {
            out.println("Sorry, you have lost all your territories, you lose the game !");
            return true;
        }
        return false;
    }

    private boolean checkWin() {
        if (status == Status.playerStatus.WIN) {
            out.println("Hooray! You win the game !");
            return true;
        }
        return false;
    }
}
