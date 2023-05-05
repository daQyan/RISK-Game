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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * This class handles the HTTP requests for the unit allocation phase of the game.
 * It is responsible for parsing the JSON request, allocating the units to the territories,
 * and waiting for all players to finish their allocation before moving on to the next phase.
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/game")
public class UnitAllocationControllerEVO2 {
    @Autowired
    public GameRepository gameRepository;
    private final Lock lock = new ReentrantLock();
    private final Condition allocationComplete = lock.newCondition();

    /**
     * This method handles the HTTP request for the unit allocation phase of the game.
     * It parses the JSON request, allocates the units to the territories, and waits for all
     * players to finish their allocation before moving on to the next phase.
     *
     * @param gameId         the ID of the game
     * @param allocationJSON the JSON request containing the player and territories
     * @return an HTTP response entity indicating the success or failure of the request
     * @throws InterruptedException if the thread is interrupted while waiting for other players to finish allocation
     */
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
        String mapJSON = jsonObject.getString("territories");
        Player player = JSON.parseObject(playerJSON, Player.class);
        List<Territory> territories = JSON.parseArray(mapJSON, Territory.class);
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
