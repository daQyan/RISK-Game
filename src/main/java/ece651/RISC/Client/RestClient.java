package ece651.RISC.Client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.Model.OnlineClient2Server;
import ece651.RISC.shared.*;
import ece651.RISC.shared.JSONConvertor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class RestClient {
    private final String server = "http://127.0.0.1:8080";
    private final RestTemplate rest;
    private final HttpHeaders headers;
    private HttpStatus httpStatus;
    private final RestClientPlayer player;
    OnlineClient2Server msgMaker;
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

    public String get(String uri) {
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
//        this.setStatus((HttpStatus) responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public String post(String uri, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
        this.setHttpStatus((HttpStatus) responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int parsePlayerId(String playerIdJSON) {
        JSONObject jsonObj = JSON.parseObject(playerIdJSON);
        return jsonObj.getInteger("player_id");
    }

    public void idHandler(int playerId){
        player.setId(playerId);
    }

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

    public void setMapToPlayer(GameMap map) {
        player.setMap(map);
    }

    public int parseUnitNum(String unitNumJSON) {
        JSONObject jsonObj = JSON.parseObject(unitNumJSON);
        return jsonObj.getInteger("init_units");
    }

    public void unitNumHandler(int unitNum){
        player.setInitUnits(unitNum);
    }

    public Status.gameStatus parseGameStatus(String oneTurnResultJSON) {
        JSONObject jsonObj = JSON.parseObject(oneTurnResultJSON);
        String statusStr = jsonObj.getString("game_status");
        return Status.gameStatus.valueOf(statusStr);
    }

    public Status.playerStatus parsePlayerStatus(String oneTurnResultJSON) {
        JSONObject jsonObj = JSON.parseObject(oneTurnResultJSON);
        String statusStr = jsonObj.getString("player_status");
        return Status.playerStatus.valueOf(statusStr);
    }

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

    public void allocation(){
        // restClient.POST("/allocation");
        ArrayList<Territory> territories = player.initUnitPlacement();
        String territoriesJSON = post("/allocation", msgMaker.allocationMsg(player, territories));
        GameMap gameMap = parseGameMap(territoriesJSON);
        setMapToPlayer(gameMap);
        player.displayMap();
    }

    public void playing(){
        // restClient.POST("/playing");
        Status.gameStatus gameStatus = Status.gameStatus.PLAYING;
        while(gameStatus != Status.gameStatus.FINISHED) {
            player.readActions();
            String oneTurnResultJSON = post("/playing", msgMaker.actionsMsg(player, player.getMoveActions(), player.getAttackActions()));
            gameStatus = parseGameStatus(oneTurnResultJSON);
            Status.playerStatus playerStatus = parsePlayerStatus(oneTurnResultJSON);
            player.setStatus(playerStatus);
            GameMap gameMap = parseGameMap(oneTurnResultJSON);
            setMapToPlayer(gameMap);
            player.displayMap();
        }
        if(player.getStatus() == Status.playerStatus.WIN || player.getStatus() == Status.playerStatus.LOSE){
            System.out.println(player.getName() + player.getStatus() + " !");
        }
    }
    public static void main(String[] args) {
        RestClient restClient = new RestClient();
        restClient.startConnect();
        restClient.allocation();
        restClient.playing();
    }
}