package com.tsoyuzhu.go.domain.api;

public class GameMoveResponse {
    private String gameId;
    private EnumGameMoveResponseType gameMoveResponseType;
    private String details;

    public GameMoveResponse(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public EnumGameMoveResponseType getGameMoveResponseType() {
        return gameMoveResponseType;
    }

    public void setGameMoveResponseType(EnumGameMoveResponseType gameMoveResponseType) {
        this.gameMoveResponseType = gameMoveResponseType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
