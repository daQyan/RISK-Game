package ece651.RISC.Server.Controller;

import ece651.RISC.Server.Model.Game;
import ece651.RISC.shared.Player;
import ece651.RISC.shared.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
public class UnitAllocationController {
    @Autowired
    public Game serverGame;

    @GetMapping("/allocation")
    public String greeting(
            @RequestParam(value = "data", defaultValue = "Nick") String reqJSON) {
        // check status first
        if (serverGame.getStatus() != Status.gameStatus.WAITINGPLAYERALLOCATE) {
            // return HTTP.serverError;
            return "error";
        }

        // TODO: handle reqJSON
        // {"id":1,"name":"test","num_units":10,"owner":{"id":1,"name":"cp","status":3},"unit":10}
        int playerId = 1;
        Set<Integer> territoriesId = new HashSet<>();
        territoriesId.add(1);
        int unit = 10;
        serverGame.AllocateUnitFromPlayer(playerId, territoriesId, unit);
        // server2Client
        // TODO: return updated player info and map info
        Player updatedPlayer = serverGame.getPlayer(playerId);
        return updatedPlayer.toJSON();
    }
}
