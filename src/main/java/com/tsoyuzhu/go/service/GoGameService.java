package com.tsoyuzhu.go.service;

import com.tsoyuzhu.go.domain.api.EnumGameMoveResponseType;
import com.tsoyuzhu.go.domain.api.GameMoveRequest;
import com.tsoyuzhu.go.domain.api.GameMoveResponse;
import com.tsoyuzhu.go.domain.gameLogic.BoardState;
import com.tsoyuzhu.go.domain.gameLogic.GoGame;
import com.tsoyuzhu.go.repository.GoGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoGameService {

    @Autowired
    private GoGameRepository goGameRepository;

    public GoGame createGame() {
        GoGame goGame = new GoGame();
        goGame.setBoardState(new BoardState());
        return goGame;
    }

    public void makeMove(GameMoveRequest request) throws Exception {
        GameMoveResponse gameMoveResponse = new GameMoveResponse(request.getGameId());

        try {
            Optional<GoGame> gameStateExists = goGameRepository.findGoGameById(request.getGameId());
            if (gameStateExists.isPresent()) {
                GoGame goGame = gameStateExists.get();
                // Update board state
                goGame.getBoardState().handleMoveRequest(request.getGameMove());
                goGame.getHistory().add(request.getGameMove());
                goGameRepository.updateGoGame(goGame);
                gameMoveResponse.setGameMoveResponseType(EnumGameMoveResponseType.SUCCESSFUL);
            } else {
                throw new Exception("Move request is invalid - Game does not exist with requested gameId");
            }
        } catch (Exception e) {
            gameMoveResponse.setGameMoveResponseType(EnumGameMoveResponseType.FAILED);
            gameMoveResponse.setDetails(e.getMessage());
        }

    }
}



