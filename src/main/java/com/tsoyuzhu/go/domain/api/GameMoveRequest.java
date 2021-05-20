package com.tsoyuzhu.go.domain.api;

import com.tsoyuzhu.go.domain.gameLogic.EnumMoveType;
import com.tsoyuzhu.go.domain.gameLogic.EnumPlayer;
import com.tsoyuzhu.go.domain.gameLogic.GameMove;

public class GameMoveRequest {
    private String gameId;
    private EnumPlayer player;
    private EnumMoveType enumMoveType;
    private GameMove gameMove;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public EnumPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EnumPlayer player) {
        this.player = player;
    }

    public EnumMoveType getEnumMoveType() {
        return enumMoveType;
    }

    public void setEnumMoveType(EnumMoveType enumMoveType) {
        this.enumMoveType = enumMoveType;
    }

    public GameMove getGameMove() {
        return gameMove;
    }

    public void setGameMove(GameMove gameMove) {
        this.gameMove = gameMove;
    }
}
