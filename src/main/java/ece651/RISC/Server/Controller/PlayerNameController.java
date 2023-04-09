package ece651.RISC.Server.Controller;

import com.alibaba.fastjson2.JSON;
import ece651.RISC.Server.Model.OnlineServer2Client;
import ece651.RISC.Server.Model.Game;
import ece651.RISC.shared.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PlayerNameController {
    @Autowired
    public Game serverGame;
    @Autowired
    public OnlineServer2Client msgMaker;

    @GetMapping("/name")
    public String greeting(@RequestBody String playerJSON) {
        // transform json to Object to get player name
        Player player = JSON.parseObject(playerJSON, Player.class);
        System.out.println("receivePlayerName" + player.toJSON());
        int playerId = serverGame.addPlayer(player);
        return msgMaker.initializationMsg(serverGame.getMyMap(), 30, playerId);
    }
}