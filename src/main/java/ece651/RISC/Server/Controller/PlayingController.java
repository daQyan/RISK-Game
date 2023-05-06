package ece651.RISC.Server.Controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.Service.Game;
import ece651.RISC.Server.Service.OnlineServer2Client;
import ece651.RISC.shared.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Controller for handling requests related to playing the game.
 * This controller is responsible for handling POST requests to the /playing endpoint.
 * <p>
 * This controller is annotated with @RestController, which indicates that it is a Spring MVC controller
 * that handles requests and returns responses as JSON.
 * <p>
 * This controller is also annotated with @CrossOrigin, which allows cross-origin requests from any domain.
 * <p>
 * The controller has two autowired dependencies: Game and OnlineServer2Client.
 * <p>
 * This controller uses a Lock and a Condition object to wait for all players to complete their turn
 * before returning the game state to the clients.
 */
@Slf4j
@RestController
@CrossOrigin
public class PlayingController {
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
     * The Lock object used for synchronization.
     */

    private final Lock lock = new ReentrantLock();
    /**
     * The Condition object used for signaling.
     */
    private final Condition actionComplete = lock.newCondition();

    /**
     * Handles POST requests to the /playing endpoint.
     * <p>
     * This method takes a JSON string in the request body and parses it into player, moveActions,
     * attackActions, upgradeTechActions, and upgradeUnitActions objects.
     * It then passes these objects to the serverGame instance to handle the actions and
     * updates the game state.
     * <p>
     * After handling the actions, the method waits for all players to complete their turn before
     * returning the game state to the clients.
     *
     * @param actionsJSON the JSON string representing the player actions
     * @return the updated game state as a JSON string
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @PostMapping("/playing")
    public String greeting(@RequestBody String actionsJSON) throws InterruptedException {
        // check status first
        if (serverGame.getStatus() != Status.gameStatus.PLAYING) {
            // return HTTP.serverError;
            return "error";
        }

        System.out.println("receiveActions" + actionsJSON);
        JSONObject jsonObject = JSON.parseObject(actionsJSON);
        String playerJSON = jsonObject.getString("player");
        Player player = JSON.parseObject(playerJSON, Player.class);
        // parse actions from json
        String moveActionsJSON = jsonObject.getString("moveActions");
        String attackActionsJSON = jsonObject.getString("attackActions");
        String upgradeTechLevelJSON = jsonObject.getString("upgradeTechActions");
        String upgradeUnitLevelJSON = jsonObject.getString("upgradeUnitActions");
        List<MoveAction> moveActions = JSON.parseArray(moveActionsJSON, MoveAction.class);
        List<AttackAction> attackActions = JSON.parseArray(attackActionsJSON, AttackAction.class);
        List<UpgradeTechAction> upgradeTechActions = JSON.parseArray(upgradeTechLevelJSON, UpgradeTechAction.class);
        List<UpgradeUnitAction> upgradeUnitActions = JSON.parseArray(upgradeUnitLevelJSON, UpgradeUnitAction.class);


        System.out.println(serverGame.getOperatedPlayerNum() + " serverGame.getOperatedPlayerSize()");
        lock.lock();
        try {
            if (serverGame.getOperatedPlayerNum() < 3) {
                actionComplete.await();
            } else {
                actionComplete.signalAll();
            }
        } finally {
            lock.unlock();
        }

        return msgMaker.oneTurnMsg(serverGame.getMyMap(), serverGame.getPlayerById(player.getId()).getStatus(), serverGame.getStatus());
    }
}
