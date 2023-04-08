package ece651.RISC.Server.Controller;

import ece651.RISC.Server.Model.Game;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RestController
public class PlayingController {
    @Autowired
    public Game serverGame;

    private final CountDownLatch latch = new CountDownLatch(3);

    @GetMapping("/playing")
    public String greeting(
            @RequestParam(value = "data", defaultValue = "Nick") String reqJSON) throws InterruptedException {
        // check status first
        if (serverGame.getStatus() != Status.gameStatus.PLAYING) {
            // return HTTP.serverError;
            return "error";
        }



        // TODO: handle reqJSON
        // {"id":1,"name":"test","num_units":10,"owner":{"id":1,"name":"cp","status":3},"unit":10}
        int playerId = 1;
        Set<Integer> territoriesId = new HashSet<>();
        territoriesId.add(1);
        int unit = 10;

        serverGame.OperateFromPlayer(); //playerId, territoriesId, unit

        if (serverGame.getOperatedPlayerSize() < 3) {
            latch.await();
        } else {
            latch.countDown();
        }


        // server2Client
        // TODO: return updated player info and map info
        Player updatedPlayer = serverGame.getPlayer(playerId);
        return updatedPlayer.toJSON();
    }
}
