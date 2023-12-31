package ece651.RISC.Server.EVO2Controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The GameController class handles the game-related REST API requests from clients.
 * It provides methods for creating a new game, joining an existing game, retrieving
 * a list of all games, and retrieving the game information for a specific game.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/game")
@Slf4j
public class GameController {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new game with the specified room size and initial unit.
     *
     * @param createGameRequest the request object containing the room size and initial unit
     * @return the response object containing the new game ID and an HTTP status code
     */
    @PostMapping("/create")
    public ResponseEntity<CreateGameResponse> createGame(@RequestBody CreateGameRequest createGameRequest) {
        // create a new game with the specified room size and initial unit
        long gameId = gameRepository.createGame(createGameRequest.getRoomSize(), createGameRequest.getInitialUnit());
        // create a response object containing the new game ID
        CreateGameResponse response = new CreateGameResponse(gameId);

        // return the response object and HTTP status code
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Joins an existing game with the specified game ID and user ID.
     *
     * @param gameId          the ID of the game to join
     * @param joinGameRequest the request object containing the user ID
     * @return an HTTP response entity containing a success or error message and an HTTP status code
     */
    @PostMapping("/{gameId}/join")
    public synchronized ResponseEntity<?> joinGame(@PathVariable Long gameId, @RequestBody JoinGameRequest joinGameRequest) {
        long userId = joinGameRequest.getUserId();
        User user = userRepository.getUser(userId);
        Game game = gameRepository.getGameById(gameId);

        if (game == null) {
            return new ResponseEntity<>("There's no such game.", HttpStatus.BAD_REQUEST);
        }
        if (!game.getStatus().equals(Status.gameStatus.WAITINGPLAYER)) {
            return new ResponseEntity<>("Game is in invalid status.", HttpStatus.BAD_REQUEST);
        }

        String msg = game.tryAddPlayer((int) userId, user.getUsername());
        if (msg == null) {
            LOGGER.info("Player " + userId + " joined game " + gameId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            LOGGER.error("Player " + userId + " joined game " + gameId + " failed, " + msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns a list of all available games.
     *
     * @return an HTTP response entity containing a list of game info objects and an HTTP status code
     */
    @GetMapping("/all")
    public ResponseEntity<GetAllGamesResponse> allRooms() {
        Map<Long, Game> games = gameRepository.getAllGames();
        List<GameInfo> gameInfos = new ArrayList<>();
        for (Long gameId : games.keySet()) {
            Game game = games.get(gameId);
            // parse game map into JSon format
            String territoriesJSON = JSON.toJSONString(game.getMyMap().getTerritories());
            // parse game player into JSON format
            String playersJSON = JSON.toJSONString(game.getPlayers());
            GameInfo gameInfo = new GameInfo(territoriesJSON, playersJSON, gameId, game.getPlayerSize(), game.getPlayers().size(), game.getPlayerInitUnits(), game.getStatus());
            gameInfos.add(gameInfo);
        }
        GetAllGamesResponse response = new GetAllGamesResponse(gameInfos);
        return ResponseEntity.ok(response);
    }

    /**
     * Returns information about the game with the specified game ID.
     *
     * @param gameId the ID of the game to get information about
     * @return an HTTP response entity containing a game info object and an HTTP status code
     */
    @GetMapping("/{gameId}/game_info")
    public ResponseEntity<GetGameInfoResponse> getGameInfo(@PathVariable Long gameId) {
        Map<Long, Game> games = gameRepository.getAllGames();
        Game game = games.get(gameId);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // parse game map into JSon format
        String territoriesJSON = JSON.toJSONString(game.getMyMap().getTerritories(), JSONWriter.Feature.PrettyFormat);
        System.out.println(territoriesJSON);
        // parse game player into JSON format
        String playersJSON = JSON.toJSONString(game.getPlayers());
        GameInfo gameInfo = new GameInfo(territoriesJSON, playersJSON, gameId, game.getPlayerSize(), game.getPlayers().size(), game.getPlayerInitUnits(), game.getStatus());
        GetGameInfoResponse response = new GetGameInfoResponse(gameInfo);
        return ResponseEntity.ok(response);
    }
}

