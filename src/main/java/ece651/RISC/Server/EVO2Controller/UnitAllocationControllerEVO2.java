package ece651.RISC.Server.EVO2Controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.Repository.GameRepository;
import ece651.RISC.Server.Service.Game;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import ece651.RISC.shared.Territory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class UnitAllocationControllerEVO2 {
    @Autowired
    public GameRepository gameRepository;
    private final Lock lock = new ReentrantLock();
    private final Condition allocationComplete = lock.newCondition();
    @PostMapping("{gameId}/allocation")
    public ResponseEntity<?> allocationUnitsEVO2(@PathVariable Long gameId, @RequestBody String allocationJSON) throws InterruptedException {
        Game serverGame = gameRepository.getGameById(gameId);
        // check status first
        if (serverGame.getStatus() != Status.gameStatus.WAITINGPLAYERALLOCATE) {
            return new ResponseEntity<>("Invalid game status", HttpStatus.BAD_REQUEST);
        }
        JSONObject jsonObject = JSON.parseObject(allocationJSON);
        // allocation Json has player and territories
        String playerJSON = jsonObject.getString("player");
        String territoriesJSON = jsonObject.getString("territories");
        Player player = JSON.parseObject(playerJSON, Player.class);
        List<Territory> territories = JSON.parseArray(territoriesJSON, Territory.class);
        serverGame.playerAllocate(player, (ArrayList<Territory>) territories);

        lock.lock();
        try {
            if (serverGame.getAllocatedPlayerSize() < serverGame.getPlayerSize()) {
                allocationComplete.await();
            } else {
                allocationComplete.signalAll();
            }
        } finally {
            lock.unlock();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
