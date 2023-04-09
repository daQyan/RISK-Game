package ece651.RISC.Server.Controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import ece651.RISC.Server.Model.Game;
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

@Slf4j
@RestController
public class UnitAllocationController {
    @Autowired
    public Game serverGame;

    @GetMapping("/allocation")
    public String greeting(@RequestBody String allocationJSON) {
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

        // server2Client
        // TODO: return updated player info and map info
        Player updatedPlayer = serverGame.getPlayer(player.getId());
        return updatedPlayer.toJSON();
    }
}
