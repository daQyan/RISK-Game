package ece651.RISC.Server.EVO2Controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.Repository.GameRepository;
import ece651.RISC.Server.Service.Game;
import ece651.RISC.shared.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * This class handles the playing actions of the game, including move, attack, upgrade tech level and upgrade unit level
 * for a player in a specific game.
 */

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/game")
public class PlayingControllerEVO2 {
    @Autowired
    public GameRepository gameRepository;
    private final Lock lock = new ReentrantLock();
    private final Condition actionComplete = lock.newCondition();

    /**
     * This method handles the playing actions of a player in a game.
     *
     * @param actionsJSON the JSON string containing the player's actions
     * @param gameId      the ID of the game the player is in
     * @return an HTTP response with status code 200 if the request is successful, or an error message and status code 400 if the request is invalid
     * @throws InterruptedException if the thread is interrupted while waiting for other players to finish their actions
     */
    @PostMapping("{gameId}/playing")
    public ResponseEntity<?> PlayingEVO2(@RequestBody String actionsJSON, @PathVariable Long gameId) throws InterruptedException {
        Game serverGame = gameRepository.getGameById(gameId);
        if (serverGame.getStatus() != Status.gameStatus.PLAYING) {
            return new ResponseEntity<>("Invalid game status", HttpStatus.BAD_REQUEST);
        }

        // parse player from json
        JSONObject jsonObject = JSON.parseObject(actionsJSON);
        String playerJSON = jsonObject.getString("player");
        Player player = JSON.parseObject(playerJSON, Player.class);
        // parse actions from json
        String moveActionsJSON = jsonObject.getString("moveActions");
        List<MoveAction> moveActions = JSON.parseArray(moveActionsJSON, MoveAction.class);

        String attackActionsJSON = jsonObject.getString("attackActions");
        List<AttackAction> attackActions = JSON.parseArray(attackActionsJSON, AttackAction.class);

        String upgradeTechLevelJSON = jsonObject.getString("upgradeTechActions");
        List<UpgradeTechAction> upgradeTechActions = JSON.parseArray(upgradeTechLevelJSON, UpgradeTechAction.class);

        String upgradeUnitLevelJSON = jsonObject.getString("upgradeUnitActions");
        List<UpgradeUnitAction> upgradeUnitActions = JSON.parseArray(upgradeUnitLevelJSON, UpgradeUnitAction.class);

        String formAllyActionsJSON = jsonObject.getString("formAllyActions");
        List<FormAllyAction> formAllyActions = JSON.parseArray(formAllyActionsJSON, FormAllyAction.class);

        serverGame.handleActions(player, (ArrayList<MoveAction>) moveActions, (ArrayList<AttackAction>) attackActions,
                (ArrayList<UpgradeTechAction>) upgradeTechActions, (ArrayList<UpgradeUnitAction>) upgradeUnitActions, (ArrayList<FormAllyAction>) formAllyActions) ;

        // until all players have finished their actions
        lock.lock();
        try {
            if (serverGame.getOperatedPlayerNum() < serverGame.getPlayerSize()) {
                actionComplete.await();
            } else {
                actionComplete.signalAll();
            }
        } finally {
            lock.unlock();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
