package ece651.RISC.Client;

import com.alibaba.fastjson2.annotation.JSONField;
import ece651.RISC.shared.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/***
 * The RestClientPlayer class represents a player that can interact with the game through a client application.
 * It extends the abstract Player class and overrides its methods to enable player actions, such as moving and attacking.
 */
public class RestClientPlayer extends Player {

    private final BufferedReader inputReader;
    private final PrintStream out;
    private int initUnits;
    private Status.playerStatus status;
    private MapTextView view;
    private GameMap map;

    public GameMap getMap() {
        return map;
    }

    /**
     * Sets the GameMap for the player and initializes the MapTextView for displaying the map.
     * Sets the player's territories to be the territories on the map that are owned by this player.
     *
     * @param map The GameMap to be set for the player.
     */
    public void setMap(GameMap map) {
        this.map = map;
        this.view = new MapTextView(map);
        setTerritories(new ArrayList<>());
        for (Territory t : map.getTerritories()) {
            if (this.equals(t.getOwner())) {
                addTerritories(t);
            }
        }
    }


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

    /**
     * Constructs a RestClientPlayer with the specified BufferedReader and PrintStream.
     *
     * @param inputReader The BufferedReader to be used for input.
     * @param out         The PrintStream to be used for output.
     */
    public RestClientPlayer(BufferedReader inputReader, PrintStream out) {
        super();
        this.inputReader = inputReader;
        this.out = out;
        this.status = Status.playerStatus.INIT;
    }

    /**
     * Prompts the player to enter their name and sets it as the player's name.
     */
    public void readPlayerName() {
        try {
            while (true) {
                out.println("WELCOME TO THE GAME. Please give yourself a name: ");

                String myName = inputReader.readLine();
                this.setName(myName);
                break;
            }
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

    /**
     * Returns the Territory object with the specified name, or null if it does not exist.
     *
     * @param terName The name of the Territory to be returned.
     * @return The Territory object with the specified name, or null if it does not exist.
     */
    public Territory getTerritoryByName(String terName) {
        if (terName == null) return null;
        for (Territory t : map.getTerritories()) {
            if (terName.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    /**
     * Method that checks and returns the source territory selected by the player.
     *
     * @param checker       an instance of ActionChecker to check if the player's move is valid
     * @param sourceTerMesg a message prompt to ask the player to enter the name of the source territory
     * @return the Territory object corresponding to the source territory entered by the player
     * @throws IOException if there's an issue reading input from the player
     */
    private Territory checkSource(ActionChecker checker, String sourceTerMesg) throws IOException {
        Territory source;
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

    /**
     * Method that checks and returns the target territory selected by the player.
     *
     * @param checker       an instance of ActionChecker to check if the player's move is valid
     * @param targetTerMesg a message prompt to ask the player to enter the name of the target territory
     * @param source        the source territory of the move or attack
     * @param status        the type of action being performed, move or attack
     * @return the Territory object corresponding to the target territory entered by the player
     * @throws IOException if there's an issue reading input from the player
     */
    private Territory checkTarget(ActionChecker checker, String targetTerMesg, Territory source, Status.actionStatus status) throws IOException {
        Territory target;
        while (true) {
            try {
                out.println(targetTerMesg);
                String targetTer = inputReader.readLine();
                target = getTerritoryByName(targetTer);
                String result;
                if (status == Status.actionStatus.MOVE) {
                    result = checker.checkAccess(source, target);
                } else result = checker.checkAtkTarget(source, target);
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

    /**
     * Method that checks and returns the number of units selected by the player for a move or attack.
     *
     * @param checker         an instance of ActionChecker to check if the player's move is valid
     * @param unitNumMesg     a message prompt to ask the player to enter the number of units
     * @param sourceTerritory the source territory of the move or attack
     * @param type            the type of action being performed, move or attack
     * @return the number of units entered by the player
     * @throws IOException if there's an issue reading input from the player
     */
    private int checkUnits(ActionChecker checker, String unitNumMesg, Territory sourceTerritory, String type) throws IOException {
        int units;
        while (true) {
            try {
                out.println(unitNumMesg);
                units = Integer.parseInt(inputReader.readLine());
                String result = checker.checkUnits(sourceTerritory, units, type);
                if (result != null) {
                    throw new IllegalArgumentException(result);
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return units;
    }

    /**
     * Allows the player to move units between their own territories.
     * Checks the validity of the source territory, target territory, and number of units to move.
     * Creates and adds a MoveAction to the player's list of actions.
     *
     * @throws IOException If an I/O error occurs while reading user input.
     */

    public void move() throws IOException {
        String welcome = "Player " + this.name + ", you can move units within your accessible territories: " + getMyTerritoryName();
        String sourceTerMesg = "Please specify the source territory with the territory name: ";
        String targetTerMesg = "Please specify the target territory with the territory name: ";
        String unitNumMesg = "Please specify the number of units to move";
        out.println(welcome);

        Territory source;
        Territory target;
        int unitMove;

        ActionChecker checker = new ActionChecker();
        source = checkSource(checker, sourceTerMesg);
        target = checkTarget(checker, targetTerMesg, source, Status.actionStatus.MOVE);
        unitMove = checkUnits(checker, unitNumMesg, source, "move");

        MoveAction move = new MoveAction(source, target, unitMove, Status.actionStatus.MOVE, this);
        move.moveOut(); // check done here
        moveActions.add(move);

        out.println("Move Successful ~");
        out.println("**-------------------------------------------------------------------------------------**");
    }

    /**
     * Allows the player to attack an enemy territory.
     * Checks the validity of the source territory, target territory, and number of units to use in the attack.
     * Creates and adds an AttackAction to the player's list of actions.
     *
     * @throws IOException If an I/O error occurs while reading user input.
     */
    public void attack() throws IOException {
        String welcome = "Player " + this.name + ", which territory would you want to attack? " + getMyTerritoryName();
        String sourceTerMesg = "Please specify the source territory with the territory name: ";
        String targetTerMesg = "Please specify the target territory with the territory name: ";
        String unitNumMesg = "Please specify the number of units use to attack";
        out.println(welcome);
        Territory source;
        Territory target;
        int unitAttack;

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

    /**
     * Returns the player's owned territories as a string, formatted for display to the user.
     *
     * @return The player's owned territories as a string.
     */

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

    /**
     * Initializes unit placement for each territory for the player.
     * Prompts the player to enter the number of units they want to place
     * in each of their territories and updates the territories accordingly.
     *
     * @return ArrayList of territories owned by the player
     */
    public ArrayList<Territory> initUnitPlacement() {
        displayMap();
        int numTer = this.territories.size();
        String prompt = "Hi~ Player " + this.name + ", you have in total " + initUnits + " units and " + numTer + " territory, please specify the units for "
                + getMyTerritoryName() + "with the format: <unit1> <unit2> ... <unitN>, where N is the number of your territory. ";
        String unitsInput;
        out.println(prompt);
        ArrayList<Territory> terList;
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
                out.println("initUnitPlacement" + getTerritories().size());
                terList = parseUnitsPlacement(unitsInput);
                break;
            } catch (IllegalArgumentException | IOException e) {
                out.println("initUnitPlacement" + e.getStackTrace());
                out.println(e.getMessage());
            }
        }
        out.println("Unit Placement Success!");
        out.println("**-------------------------------------------------------------------------------------**");
        return terList;
    }

    /**
     * Parses the input from user and updates the units for each territory owned by the player.
     *
     * @param prompt input string from user
     * @return ArrayList of territories owned by the player with updated units
     * @throws IllegalArgumentException if the input is not valid
     */
    // parse the input from user and update its Territories
    private ArrayList<Territory> parseUnitsPlacement(String prompt) {
        ArrayList<Territory> myTerritory = getTerritories();
        ArrayList<Integer> numList = new ArrayList<>();
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
        } else {
            throw new IllegalArgumentException("Total input units beyond or below scope!\n");
        }

        return myTerritory;
    }

    /**
     * Clear the previous move and attack actions, and read new actions from the player
     */
    // player play one turn with move and attack orders
    public void readActions() {
        moveActions.clear();
        attackActions.clear();
        if (checkLose()) return;
        if (checkWin()) return;
        // keep receiving order input until (D)one
        try {
            while (true) {
//                view.displayMap();
                out.println(name + ", your options: M for move, A for attack, D for Done");
                String s = inputReader.readLine().toUpperCase();
                if (s.equals("D")) {
                    out.println("D" + moveActions.size());
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
        } catch (IOException error) {
            out.println(error.getMessage());
        }
    }

    /**
     * Get the move actions made by the player
     *
     * @return an ArrayList of MoveAction
     */
    public ArrayList<MoveAction> getMoveActions() {
        return moveActions;
    }

    /**
     * Get the attack actions made by the player
     *
     * @return an ArrayList of AttackAction
     */
    public ArrayList<AttackAction> getAttackActions() {
        return attackActions;
    }

    /**
     * Check if the player has lost the game by losing all of their territories
     *
     * @return true if the player has lost, false otherwise
     */
    private boolean checkLose() {
        if (status == Status.playerStatus.LOSE) {
            out.println("Sorry, you have lost all your territories, you lose the game !");
            return true;
        }
        return false;
    }

    /**
     * Check if the player has won the game by conquering all territories
     *
     * @return true if the player has won, false otherwise
     */
    private boolean checkWin() {
        if (status == Status.playerStatus.WIN) {
            out.println("Hooray! You win the game !");
            return true;
        }
        return false;
    }
}
