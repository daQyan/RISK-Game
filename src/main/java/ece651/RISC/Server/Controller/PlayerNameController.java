package ece651.RISC.Server.Controller;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import ece651.RISC.Server.ServerGame;
import ece651.RISC.shared.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PlayerNameController {
    @Autowired
    public ServerGame serverGame;
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/name")
    public Greeting greeting(
            @RequestParam(value = "player_name", defaultValue = "Nick") String name) throws IOException {
        LOGGER.info("asdasdasdsada***");

        String playerName = name;
        serverGame.addPlayer(new Player(1, playerName));
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
