package com.tsoyuzhu.go.service;

import com.tsoyuzhu.go.domain.api.EnumGameMoveResponseType;
import com.tsoyuzhu.go.domain.api.GameMoveRequest;
import com.tsoyuzhu.go.domain.api.GameMoveResponse;
import com.tsoyuzhu.go.domain.gameLogic.GoGame;
import com.tsoyuzhu.go.repository.GoGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoGameService {

    @Autowired
    private GoGameRepository goGameRepository;

    public GoGame createGame() {
        GoGame goGame = new GoGame();
        return goGameRepository.createGoGame(goGame);
    }

    public Optional<GoGame> getGameById(String id) {
        return goGameRepository.findGoGameById(id);
    }

    public List<GoGame> getAllGames() {
        return goGameRepository.findAllGames();
    }

    // Games can only be modified using GameMoveRequests
    public GameMoveResponse updateGame(GameMoveRequest request) {
        GameMoveResponse gameMoveResponse = new GameMoveResponse(request.getGameId());
        try {
            GoGame goGame = verifyGameMoveRequest(request);
            // Update board state
            goGame.getBoardState().handleMoveRequest(request.getGameMove());
            goGame.getHistory().add(request.getGameMove());
            goGameRepository.updateGoGame(goGame);
            gameMoveResponse.setGameMoveResponseType(EnumGameMoveResponseType.SUCCESSFUL);
        } catch (Exception e) {
            gameMoveResponse.setGameMoveResponseType(EnumGameMoveResponseType.FAILED);
            gameMoveResponse.setDetails(e.getMessage());
        }
        return gameMoveResponse;
    }

    public void deleteGame(String id) throws Exception {
        Optional<GoGame> existing = goGameRepository.findGoGameById(id);
        if (existing.isPresent()) {
            goGameRepository.deleteGameById(id);
        } else {
            throw new Exception("GoGame with id " + id + " does not exist - Cannot delete");
        }
    }

    private GoGame verifyGameMoveRequest(GameMoveRequest gameMoveRequest) throws Exception {
        checkNotNull(gameMoveRequest.getGameMove(), "gameMove");
        checkNotNull(gameMoveRequest.getGameMove().getPosition(), "position");
        checkNotNull(gameMoveRequest.getGameMove().getMoveType(), "moveType");
        checkNotNull(gameMoveRequest.getGameMove().getPieceType(), "pieceType");
        checkNotNull(gameMoveRequest.getGameMove().getPlayer(), "gameMove");
        checkNotNull(gameMoveRequest.getGameId(), "gameId");
        Optional<GoGame> gameStateExists = goGameRepository.findGoGameById(gameMoveRequest.getGameId());
        if (!gameStateExists.isPresent()) {
            throw new Exception("GameMove invalid - Game does not exist with requested gameId");
        }
        return gameStateExists.get();
    }

    private void checkNotNull(Object o, String fieldName) throws Exception {
        if (o == null) {
            throw new Exception("GameMove Invalid - " + fieldName + " was not provided in request");
        }
    }
}



