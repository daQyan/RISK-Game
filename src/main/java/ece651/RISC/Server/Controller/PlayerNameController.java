package ece651.RISC.Server.Controller;

import com.alibaba.fastjson2.JSON;
import ece651.RISC.Server.Service.Game;
import ece651.RISC.Server.Service.OnlineServer2Client;
import ece651.RISC.shared.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling requests related to player name and game state.
 * This controller is responsible for handling POST requests to the /player_name endpoint
 * and GET requests to the /map and /unit_num endpoints.
 * <p>
 * This controller is annotated with @RestController, which indicates that it is a Spring MVC controller
 * that handles requests and returns responses as JSON.
 * <p>
 * This controller is also annotated with @CrossOrigin, which allows cross-origin requests from any domain.
 * <p>
 * The controller has two autowired dependencies: Game and OnlineServer2Client.
 */
@Slf4j
@RestController
@CrossOrigin
public class PlayerNameController {
    /**
     * The Game instance, autowired by Spring.
     */
    @Autowired
    public Game serverGame;
    /**
     * The OnlineServer2Client instance, autowired by Spring.
     */
    @Autowired
    public OnlineServer2Client msgMaker;

    /**
     * Handles POST requests to the /player_name endpoint.
     * <p>
     * This method takes a JSON string in the request body and parses it into a Player object.
     * It then adds the player to the game and returns the player ID as a JSON string.
     *
     * @param playerNameJSON the JSON string representing the player name
     * @return the player ID as a JSON string
     */

    @PostMapping("/player_name")
    public String getPlayerIdJSON(@RequestBody String playerNameJSON) {
        // transform json to Object to get player name
        Player player = JSON.parseObject(playerNameJSON, Player.class);
        System.out.println(" n" + player.toJSON());
        int playerId = serverGame.addPlayer(player);
        return msgMaker.playerIdMsg(playerId);
    }

    /**
     * Handles GET requests to the /map endpoint.
     * <p>
     * This method returns the game map as a JSON string.
     *
     * @return the game map as a JSON string
     */
    @GetMapping("/map")
    public String getMapJSON() {
        return msgMaker.gameMapMsg(serverGame.getMyMap());
    }

    /**
     * Handles GET requests to the /unit_num endpoint.
     * <p>
     * This method returns the number of units as a JSON string.
     *
     * @return the number of units as a JSON string
     */
    @GetMapping("/unit_num")
    public String getUnitNumJSON() {
        // TODO: change it to variable
        return msgMaker.unitNumMsg(3);
    }
}