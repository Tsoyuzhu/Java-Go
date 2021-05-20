package com.tsoyuzhu.go;

import com.tsoyuzhu.go.domain.api.GameMoveRequest;
import com.tsoyuzhu.go.domain.gameLogic.BoardState;
import com.tsoyuzhu.go.domain.gameLogic.EnumPlayer;
import com.tsoyuzhu.go.domain.gameLogic.GameState;

public class GameService {

    private static final EnumPlayer GO_STARTING_PLAYER = EnumPlayer.BLACK;

    public GameState createGame() {
        GameState gameState = new GameState();
        gameState.setPlayerToMove(GO_STARTING_PLAYER);
        gameState.setBoardState(new BoardState());
        return gameState;
    }

    public void makeMove(GameMoveRequest request) {

    }


}



