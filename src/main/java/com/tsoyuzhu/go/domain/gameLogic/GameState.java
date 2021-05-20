package com.tsoyuzhu.go.domain.gameLogic;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private String gameId;

    // Player who is making a move this turn
    private EnumPlayer playerToMove;

    // List of past moves in the current game
    private List<GameMove> history;

    // List of all legal moves for the player to move
    private List<GameMove> legalMoves;

    // Current position of all pieces on the game board
    private BoardState boardState;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public EnumPlayer getPlayerToMove() {
        return playerToMove;
    }

    public void setPlayerToMove(EnumPlayer playerToMove) {
        this.playerToMove = playerToMove;
    }

    public List<GameMove> getHistory() {
        if (history == null) {
            return new ArrayList<GameMove>();
        }
        return history;
    }

    public void setHistory(List<GameMove> history) {
        this.history = history;
    }

    public List<GameMove> getLegalMoves() {
        if (legalMoves == null) {
            return new ArrayList<GameMove>();
        }
        return legalMoves;
    }

    public void setLegalMoves(List<GameMove> legalMoves) {
        this.legalMoves = legalMoves;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }
}
