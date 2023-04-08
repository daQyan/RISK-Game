package ece651.RISC.Server.Controller;
import java.io.IOException;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import ece651.RISC.Server.Model.Game;
import ece651.RISC.shared.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

class NameObject {
    @JSONField(name = "player_name")
    String name;

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}

@Slf4j
@RestController
public class PlayerNameController {
    @Autowired
    public Game serverGame;

    public String toJSON(int playerId){
        JSONObject json = new JSONObject();
        json.put("player_id", playerId);
        return json.toJSONString();
    }


    @GetMapping("/name")
    public String greeting(
            @RequestBody JSONObject request) {
        // TODO: transform json to Object to get player name
//        NameObject nameRequest = JSON.parseObject(request, NameObject.class);
        String playerName = request.getString("name");
        int playerId = serverGame.addPlayer(new Player(playerName));

        return toJSON(playerId);
    }
}
