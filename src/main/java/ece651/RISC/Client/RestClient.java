package ece651.RISC.Client;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Online.OnlineClient2Server;
import ece651.RISC.Server.Service.Game;
import ece651.RISC.shared.GameMap;
import ece651.RISC.shared.JSONConvertor;
import ece651.RISC.shared.Status;
import ece651.RISC.shared.Territory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class RestClient {
    private String server = "http://127.0.0.1:8080";
    private RestTemplate rest;
    private HttpHeaders headers;
    private HttpStatus httpStatus;
    private RestClientPlayer player;
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
        System.out.println("here1");
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

    public HttpStatus getHttpStatus() {
        return httpStatus;
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
        System.out.println("receiveMap" + mapJSON);
        JSONObject jsonObj = JSON.parseObject(mapJSON);
        String map = jsonObj.getString("map");

        JSONObject mapObj = JSON.parseObject(map);
        String territoriesJSON = mapObj.getString("territories");

        GameMap newMap = ece651.RISC.shared.JSONConvertor.convertTerritories(territoriesJSON);
        String adjacentsJSON = mapObj.getString("adjacents");
        ece651.RISC.shared.JSONConvertor.setRelations(adjacentsJSON, newMap, true);
        String accessiblesJSON = mapObj.getString("accessibles");
        JSONConvertor.setRelations(accessiblesJSON, newMap, false);
        System.out.println("newMap");

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

    public void gameMapHandler(int unitNum){
        player.setInitUnits(unitNum);
    }

    public Status.gameStatus parseGameStatus(String oneTurnResultJSON) {
        System.out.println("processing oneTurnResultJSON" + oneTurnResultJSON);
        JSONObject jsonObj = JSON.parseObject(oneTurnResultJSON);
        String statusStr = jsonObj.getString("game_status");
        Status.gameStatus status = Status.gameStatus.valueOf(statusStr);

        return status;
    }

    public Status.playerStatus parsePlayerStatus(String oneTurnResultJSON) {
        System.out.println("processing oneTurnResultJSON" + oneTurnResultJSON);
        JSONObject jsonObj = JSON.parseObject(oneTurnResultJSON);
        String statusStr = jsonObj.getString("player_status");
        Status.playerStatus status = Status.playerStatus.valueOf(statusStr);

        return status;
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
        System.out.println("startConnect end");
    }

    public void allocation(){
        // restClient.POST("/allocation");
        ArrayList<Territory> territories = player.initUnitPlacement();
        String territoriesJSON = post("/allocation", msgMaker.allocationMsg(player, territories));
        System.out.println("territoriesJSON" + territoriesJSON);
        GameMap gameMap = parseGameMap(territoriesJSON);
        setMapToPlayer(gameMap);
        player.displayMap();
        System.out.println("allocation end");
    }

    public void playing(){
        // restClient.POST("/playing");
        Status.gameStatus gameStatus = Status.gameStatus.PLAYING;
        while(gameStatus != Status.gameStatus.FINISHED) {
            player.readActions();
            String oneTurnResultJSON = post("/playing", msgMaker.actionsMsg(player, player.getMoveActions(), player.getAttackActions()));
            System.out.println("territoriesJSON" + oneTurnResultJSON);
            gameStatus = parseGameStatus(oneTurnResultJSON);
            Status.playerStatus playerStatus = parsePlayerStatus(oneTurnResultJSON);

            GameMap gameMap = parseGameMap(oneTurnResultJSON);
            setMapToPlayer(gameMap);
            player.displayMap();
            System.out.println("playing one turn end");
        }
    }
    public static void main(String[] args) {
        RestClient restClient = new RestClient();
        restClient.startConnect();
        restClient.allocation();
        restClient.playing();
    }
}