package ece651.RISC.Server.Controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.Model.Game;
import ece651.RISC.Server.Model.OnlineServer2Client;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import ece651.RISC.shared.Territory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;



@Slf4j
@RestController
public class UnitAllocationController {
    @Autowired
    public Game serverGame;
    @Autowired
    public OnlineServer2Client msgMaker;

    private final CountDownLatch latch = new CountDownLatch(3);

    @GetMapping("/allocation")
    public String greeting(@RequestBody String allocationJSON) throws InterruptedException {
        // check status first
        if (serverGame.getStatus() != Status.gameStatus.WAITINGPLAYERALLOCATE) {
            // return HTTP.serverError;
            return "error";
        }
        System.out.println(latch.getCount());
        latch.countDown(); // 每有一个用户访问页面，就减 1
        JSONObject jsonObject = JSON.parseObject(allocationJSON);
        System.out.println("allocationJSON " + allocationJSON);

        String playerJSON = jsonObject.getString("player");
        String territoriesJSON = jsonObject.getString("territories");
        Player player = JSON.parseObject(playerJSON, Player.class);
        List<Territory> territories = JSON.parseArray(territoriesJSON, Territory.class);
        serverGame.playerAllocate(player, (ArrayList<Territory>) territories);

        System.out.println(serverGame.getAllocatedPlayerSize() + " serverGame.getAllocatedPlayerSize() ");





        System.out.println("after latch *******");
        latch.await();     // 等待计数器为 0
        return msgMaker.allocationMsg(serverGame.getMyMap());
    }
}
