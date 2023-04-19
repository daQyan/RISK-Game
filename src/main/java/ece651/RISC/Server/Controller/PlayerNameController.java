package ece651.RISC.Server.Controller;

import com.alibaba.fastjson2.JSON;
import ece651.RISC.Server.Service.OnlineServer2Client;
import ece651.RISC.Server.Service.Game;
import ece651.RISC.shared.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PlayerNameController {
    @Autowired
    public Game serverGame;
    @Autowired
    public OnlineServer2Client msgMaker;

    @PostMapping("/player_name")
    public String getPlayerIdJSON(@RequestBody String playerNameJSON) {
        // transform json to Object to get player name
        Player player = JSON.parseObject(playerNameJSON, Player.class);
        System.out.println(" n" + player.toJSON());
        int playerId = serverGame.addPlayer(player);
        return msgMaker.playerIdMsg(playerId);
    }

    @GetMapping("/map")

    public String getMapJSON() {
        return msgMaker.gameMapMsg(serverGame.getMyMap());
    }

    @GetMapping("/unit_num")
    public String getUnitNumJSON() {
        // TODO: change it to variable
        return msgMaker.unitNumMsg(30);
    }
}