package ece651.RISC.Server.EVO2Controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.Repository.GameRepository;
import ece651.RISC.Server.Service.Game;
import ece651.RISC.Server.Service.OnlineServer2Client;
import ece651.RISC.shared.AttackAction;
import ece651.RISC.shared.MoveAction;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
@RestController
@RequestMapping("/api/game")
public class PlayingControllerEVO2 {
    @Autowired
    public GameRepository gameRepository;
    @Autowired
    public OnlineServer2Client msgMaker;
    private final Lock lock = new ReentrantLock();
    private final Condition actionComplete = lock.newCondition();
    @PostMapping("{gameId}/playing")
    public String PlayingEVO2(@RequestBody String actionsJSON, @PathVariable Long gameId) throws InterruptedException {
        Game serverGame = gameRepository.getGameById(gameId);
        // check status first
        if (serverGame.getStatus() != Status.gameStatus.PLAYING) {
            // return HTTP.serverError;
            return "error";
        }

        System.out.println("receiveActions" + actionsJSON);
        JSONObject jsonObject = JSON.parseObject(actionsJSON);
        String playerJSON = jsonObject.getString("player");
        Player player = JSON.parseObject(playerJSON, Player.class);
        String moveActionsJSON = jsonObject.getString("moveActions");
        String attackActionsJSON = jsonObject.getString("attackActions");
        List<MoveAction> moveActions = JSON.parseArray(moveActionsJSON, MoveAction.class);
        List<AttackAction> attackActions = JSON.parseArray(attackActionsJSON, AttackAction.class);
        serverGame.handleActions(player, (ArrayList<MoveAction>) moveActions, (ArrayList<AttackAction>) attackActions);
        System.out.println(serverGame.getOperatedPlayerNum() + " serverGame.getOperatedPlayerSize()");
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

        return msgMaker.oneTurnMsg(serverGame.getMyMap(), serverGame.getPlayer(player.getId()).getStatus(), serverGame.getStatus());
    }
}
