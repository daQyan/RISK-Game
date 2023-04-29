package ece651.RISC.Server.Controller;

import ece651.RISC.Server.Model.GameInfo;
import ece651.RISC.Server.Model.Payload.Request.CreateGameRequest;
import ece651.RISC.Server.Model.Payload.Request.JoinGameRequest;
import ece651.RISC.Server.Model.Payload.Response.CreateGameResponse;
import ece651.RISC.Server.Model.Payload.Response.GetAllGamesResponse;
import ece651.RISC.Server.Model.Payload.Response.GetGameInfoResponse;
import ece651.RISC.Server.Model.User;
import ece651.RISC.Server.Repository.GameRepository;
import ece651.RISC.Server.Repository.UserRepository;
import ece651.RISC.Server.Service.Game;
import ece651.RISC.shared.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
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
        if(!game.getStatus().equals(Status.gameStatus.WAITINGPLAYER)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GameInfo gameInfo = new GameInfo(gameId, game.getPlayerSize(), game.getGamePlayers().size(), game.getPlayerInitUnits(), game.getStatus());
        GetGameInfoResponse response = new GetGameInfoResponse(gameInfo);

        boolean isAdded = game.tryAddPlayer(userId, user.getUsername());
        if (isAdded) {
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<GetAllGamesResponse> allRooms() {
        Map<Long, Game> games = gameRepository.getAllGames();
        List<GameInfo> gameInfos = new ArrayList<>();
        for (Long gameId : games.keySet()) {
            Game game = games.get(gameId);
            GameInfo gameInfo = new GameInfo(gameId, game.getPlayerSize(), game.getGamePlayers().size(), game.getPlayerInitUnits(), game.getStatus());
            gameInfos.add(gameInfo);
        }
        GetAllGamesResponse response = new GetAllGamesResponse(gameInfos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{gameId}/game_info")
    public ResponseEntity<GetGameInfoResponse> getGameInfo(@PathVariable Long gameId) {
        Map<Long, Game> games = gameRepository.getAllGames();
        Game game = games.get(gameId);
        GameInfo gameInfo = new GameInfo(gameId, game.getPlayerSize(), game.getGamePlayers().size(), game.getPlayerInitUnits(), game.getStatus());
        GetGameInfoResponse response = new GetGameInfoResponse(gameInfo);
        return ResponseEntity.ok(response);
    }
}

