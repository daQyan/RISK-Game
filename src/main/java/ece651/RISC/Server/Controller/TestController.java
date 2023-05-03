package ece651.RISC.Server.Controller;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import ece651.RISC.Server.Controller.ChatWebSocketHandler;

@Controller
public class TestController {
    private final ChatWebSocketHandler chatWebSocketHandler;

    @Autowired
    public TestController(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @PostMapping("/test")
    public ResponseEntity<String> testWebSocket(@RequestBody String message) throws URISyntaxException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/chat?playerId=test";

        restTemplate.postForEntity(url, message, String.class);

        return new ResponseEntity<>("WebSocket test successful", HttpStatus.OK);
    }
}
