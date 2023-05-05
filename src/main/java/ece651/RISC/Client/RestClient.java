package ece651.RISC.Client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.Model.OnlineClient2Server;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.JSONConvertor;
import ece651.RISC.shared.Status;
import ece651.RISC.shared.Territory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * The RestClient class is responsible for connecting to the RISC game server using RESTful API calls.
 * It sends and receives messages to/from the server and updates the game state based on the received messages.
 * It also interacts with the user through console input and output.
 */
public class RestClient {
    private final String server = "http://127.0.0.1:8080";
    private final RestTemplate rest;
    private final HttpHeaders headers;
    private HttpStatus httpStatus;
    private final RestClientPlayer player;
    OnlineClient2Server msgMaker;

    /**
     * Constructor for the RestClient class.
     * It initializes the RestTemplate and HttpHeaders objects and reads console input/output streams.
     */
    public RestClient() {
        this.rest = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
        BufferedReader inputReader = new BufferedReader(new InputStreamReader((System.in)));
        PrintStream out = System.out;
        player = new RestClientPlayer(inputReader, out);
        msgMaker = new OnlineClient2Server();
    }

    /**
     * Sends a GET request to the specified URI and returns the response body as a string.
     *
     * @param uri the URI to send the GET request to
     * @return the response body as a string
     */
    public String get(String uri) {
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
//        this.setStatus((HttpStatus) responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    /**
     * Sends a POST request with the specified JSON message to the specified URI and returns the response body as a string.
     *
     * @param uri  the URI to send the POST request to
     * @param json the JSON message to send in the POST request
     * @return the response body as a string
     */
    public String post(String uri, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
        this.setHttpStatus((HttpStatus) responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    /**
     * Setter method for the HttpStatus instance variable.
     *
     * @param httpStatus the HttpStatus to set
     */
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    /**
     * Parses the player ID from the JSON response returned by the server.
     *
     * @param playerIdJSON the JSON response containing the player ID
     * @return the player ID as an integer
     */
    public int parsePlayerId(String playerIdJSON) {
        JSONObject jsonObj = JSON.parseObject(playerIdJSON);
        return jsonObj.getInteger("player_id");
    }

    /**
     * Sets the player ID to the RestClientPlayer instance.
     *
     * @param playerId the player ID to set
     */
    public void idHandler(int playerId) {
        player.setId(playerId);
    }

    /**
     * Parses a JSON string representation of a game map and returns a corresponding GameMap object.
     *
     * @param mapJSON a JSON string representation of the game map.
     * @return a GameMap object that represents the parsed game map.
     */
    public GameMap parseGameMap(String mapJSON) {
        JSONObject jsonObj = JSON.parseObject(mapJSON);
        String map = jsonObj.getString("map");

        JSONObject mapObj = JSON.parseObject(map);
        String territoriesJSON = mapObj.getString("territories");

        GameMap newMap = ece651.RISC.shared.JSONConvertor.convertTerritories(territoriesJSON);
        String adjacentsJSON = mapObj.getString("adjacents");
        ece651.RISC.shared.JSONConvertor.setRelations(adjacentsJSON, newMap, true);
        String accessiblesJSON = mapObj.getString("accessibles");
        JSONConvertor.setRelations(accessiblesJSON, newMap, false);

        return newMap;
    }

    /**
     * Sets the given game map to the player's map in the game.
     *
     * @param map the GameMap object to set to the player's map.
     */
    public void setMapToPlayer(GameMap map) {
        player.setMap(map);
    }

    /**
     * Parses a JSON string representation of the initial number of units a player has and returns the integer value.
     *
     * @param unitNumJSON a JSON string representation of the initial number of units a player has.
     * @return the integer value of the "init_units" property in the JSON object.
     */

    public int parseUnitNum(String unitNumJSON) {
        JSONObject jsonObj = JSON.parseObject(unitNumJSON);
        return jsonObj.getInteger("init_units");
    }

    /**
     * Sets the initial number of units for the player in the game.
     *
     * @param unitNum the integer value of the initial number of units for the player.
     */
    public void unitNumHandler(int unitNum) {
        player.setInitUnits(unitNum);
    }

    /**
     * Parses a JSON string representation of the game status for a player and returns the corresponding Status.gameStatus enum value.
     *
     * @param oneTurnResultJSON a JSON string representation of the game status for a player.
     * @return a Status.gameStatus enum value that represents the game status.
     */
    public Status.gameStatus parseGameStatus(String oneTurnResultJSON) {
        JSONObject jsonObj = JSON.parseObject(oneTurnResultJSON);
        String statusStr = jsonObj.getString("game_status");
        return Status.gameStatus.valueOf(statusStr);
    }

    /**
     * Parses a JSON string representation of the player status for a player and returns the corresponding Status.playerStatus enum value.
     *
     * @param oneTurnResultJSON a JSON string representation of the player status for a player.
     * @return a Status.playerStatus enum value that represents the player status.
     */

    public Status.playerStatus parsePlayerStatus(String oneTurnResultJSON) {
        JSONObject jsonObj = JSON.parseObject(oneTurnResultJSON);
        String statusStr = jsonObj.getString("player_status");
        return Status.playerStatus.valueOf(statusStr);
    }

    /**
     * Starts the connection to the game server by sending a POST request with the player's name, and then sending GET requests
     * to retrieve the game map and the number of initial units for the player. The retrieved data is then parsed and set to the player.
     */
    public void startConnect() {
        // restClient.POST("/player_name");
        player.readPlayerName();
        // respond player id
        String resPlayerIdJSON = post("/player_name", msgMaker.nameMsg(player));
        int playerId = parsePlayerId(resPlayerIdJSON);
        idHandler(playerId);


        // restClient.get("/map");
        String resMapJSON = get("/map");
        GameMap gameMap = parseGameMap(resMapJSON);
        setMapToPlayer(gameMap);

        // GET /unit_num
        String unitNumJSON = get("/unit_num");
        int unitNum = parseUnitNum(unitNumJSON);
        unitNumHandler(unitNum);
    }

    /**
     * Handles the allocation phase of the game by sending a POST request with the player's initial unit placements, and then setting
     * the game map to the player with the updated unit placements. The player's map is then displayed.
     */
    public void allocation() {
        // restClient.POST("/allocation");
        ArrayList<Territory> territories = player.initUnitPlacement();
        String territoriesJSON = post("/allocation", msgMaker.allocationMsg(player, territories));
        GameMap gameMap = parseGameMap(territoriesJSON);
        setMapToPlayer(gameMap);
        player.displayMap();
    }

    /**
     * Handles the playing phase of the game by repeatedly sending POST requests with the player's move and attack actions until the
     * game status is FINISHED. The responses are parsed and used to update the player's status, game map, and display the updated map.
     */

    public void playing() {
        // restClient.POST("/playing");
        Status.gameStatus gameStatus = Status.gameStatus.PLAYING;
        while (gameStatus != Status.gameStatus.FINISHED) {
            player.readActions();
            String oneTurnResultJSON = post("/playing", msgMaker.actionsMsg(player, player.getMoveActions(), player.getAttackActions()));
            gameStatus = parseGameStatus(oneTurnResultJSON);
            Status.playerStatus playerStatus = parsePlayerStatus(oneTurnResultJSON);
            player.setStatus(playerStatus);
            GameMap gameMap = parseGameMap(oneTurnResultJSON);
            setMapToPlayer(gameMap);
            player.displayMap();
        }
        if (player.getStatus() == Status.playerStatus.WIN || player.getStatus() == Status.playerStatus.LOSE) {
            System.out.println(player.getName() + player.getStatus() + " !");
        }
    }

    /**
     * The main entry point for the game client. Creates a new RestClient object, starts the connection to the game server,
     * handles the allocation phase, and then handles the playing phase.
     *
     * @param args the command-line arguments passed to the program (not used).
     */

    public static void main(String[] args) {
        RestClient restClient = new RestClient();
        restClient.startConnect();
        restClient.allocation();
        restClient.playing();
    }
}