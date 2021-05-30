package com.tsoyuzhu.go.domain.api;

import com.tsoyuzhu.go.domain.gameLogic.GameMove;

public class GameMoveRequest {
    private String gameId;
    private GameMove gameMove;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public GameMove getGameMove() {
        return gameMove;
    }

    public void setGameMove(GameMove gameMove) {
        this.gameMove = gameMove;
    }
}
