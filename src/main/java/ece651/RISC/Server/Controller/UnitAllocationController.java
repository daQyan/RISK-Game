package ece651.RISC.Server.Controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.Service.Game;
import ece651.RISC.Server.Service.OnlineServer2Client;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import ece651.RISC.shared.Territory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
@RestController
public class UnitAllocationController {
    @Autowired
    public Game serverGame;
    @Autowired
    public OnlineServer2Client msgMaker;
    private final Lock lock = new ReentrantLock();
    private final Condition allocationComplete = lock.newCondition();
    @PostMapping("/allocation")
    public String greeting(@RequestBody String allocationJSON) throws InterruptedException {
        // check status first
        if (serverGame.getStatus() != Status.gameStatus.WAITINGPLAYERALLOCATE) {
            // return HTTP.serverError;
            return "error";
        }
        JSONObject jsonObject = JSON.parseObject(allocationJSON);
        System.out.println("allocationJSON " + allocationJSON);

        String playerJSON = jsonObject.getString("player");
        String territoriesJSON = jsonObject.getString("territories");
        Player player = JSON.parseObject(playerJSON, Player.class);
        List<Territory> territories = JSON.parseArray(territoriesJSON, Territory.class);
        serverGame.playerAllocate(player, (ArrayList<Territory>) territories);


        lock.lock();
        try {
            if (serverGame.getAllocatedPlayerSize() < 3) {
                allocationComplete.await();
            } else {
                allocationComplete.signalAll();
            }
        } finally {
            lock.unlock();
        }

        return msgMaker.allocationMsg(serverGame.getMyMap());
    }
}
