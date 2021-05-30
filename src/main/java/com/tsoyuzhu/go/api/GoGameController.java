package com.tsoyuzhu.go.api;

import com.tsoyuzhu.go.domain.api.EnumGameMoveResponseType;
import com.tsoyuzhu.go.domain.api.GameMoveRequest;
import com.tsoyuzhu.go.domain.api.GameMoveResponse;
import com.tsoyuzhu.go.domain.gameLogic.GoGame;
import com.tsoyuzhu.go.service.GoGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/game")
public class GoGameController {

    @Autowired
    private GoGameService goGameService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<GoGame>> getAllGoGames() {
        List<GoGame> goGames = goGameService.getAllGames();
        return ResponseEntity.ok(goGames);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<GoGame> getGoGameById(@PathVariable String id) {
        Optional<GoGame> gameExists = goGameService.getGameById(id);
        return ResponseEntity.of(gameExists);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<GoGame> createGoGame() {
        return ResponseEntity.status(HttpStatus.CREATED).body(goGameService.createGame());
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<GameMoveResponse> updateGoGame(GameMoveRequest gameMoveRequest) {
        GameMoveResponse gameMoveResponse = goGameService.updateGame(gameMoveRequest);
        if (gameMoveResponse.getGameMoveResponseType() == EnumGameMoveResponseType.SUCCESSFUL) {
            return ResponseEntity.ok(gameMoveResponse);
        } else {
            return ResponseEntity.unprocessableEntity().body(gameMoveResponse);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteGoGame(@PathVariable String id) {
        try {
            goGameService.deleteGame(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
