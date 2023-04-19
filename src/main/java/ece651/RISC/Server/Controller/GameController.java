package ece651.RISC.Server.Controller;

import ece651.RISC.Server.Model.GameInfo;
import ece651.RISC.Server.Model.Payload.Request.CreateGameRequest;
import ece651.RISC.Server.Model.Payload.Request.JoinGameRequest;
import ece651.RISC.Server.Model.Payload.Response.CreateGameResponse;
import ece651.RISC.Server.Model.Payload.Response.GetAllGamesResponse;
import ece651.RISC.Server.Model.User;
import ece651.RISC.Server.Repository.GameRepository;
import ece651.RISC.Server.Repository.UserRepository;
import ece651.RISC.Server.Service.Game;
import ece651.RISC.shared.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<CreateGameResponse> createGame(@RequestBody CreateGameRequest createGameRequest) {
        // create a new game with the specified room size and initial unit
        long gameId = gameRepository.createGame(createGameRequest.getRoomSize(), createGameRequest.getInitialUnit());
        System.out.println(gameRepository.getAllGames().entrySet().size());
        // create a response object containing the new game ID
        CreateGameResponse response = new CreateGameResponse(gameId);

        // return the response object and HTTP status code
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<?> joinGame(@PathVariable Long gameId, @RequestBody JoinGameRequest joinGameRequest) {
        Long userId = joinGameRequest.getUserId();
        User user = userRepository.getUser(userId);
        Game game = gameRepository.getGameById(gameId);
        game.addPlayer(new Player(user.getUsername()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
//
//    @PostMapping("/{gameId}/start")
//    public ResponseEntity<StartGameResponse> startGame(@PathVariable Long gameId) {
//        // TODO: implement starting game logic
//        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
//    }

    @GetMapping("/all")
    public ResponseEntity<GetAllGamesResponse> allRooms() {
        Map<Long, Game> games = gameRepository.getAllGames();
        List<GameInfo> gameInfos = new ArrayList<>();
        for (Long gameId : games.keySet()) {
            Game game = games.get(gameId);
            GameInfo gameInfo = new GameInfo(gameId, game.getPlayerSize(), game.getPlayerInitUnits(), game.getStatus());
            gameInfos.add(gameInfo);
        }
        GetAllGamesResponse response = new GetAllGamesResponse(gameInfos);
        return ResponseEntity.ok(response);
    }
}