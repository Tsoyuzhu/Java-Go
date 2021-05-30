package com.tsoyuzhu.go.domain.gameLogic;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class GoGame {
    @Id
    private String id;

    // List of past moves in the current game
    private List<GameMove> history;

    // List of all legal moves for the player to move
    private List<GameMove> legalMoves;

    // Current position of all pieces on the game board
    private BoardState boardState;

    private EnumGameStatus gameStatus;

    public GoGame() {
        this.legalMoves = new ArrayList<>();
        this.history = new ArrayList<>();
        this.boardState = new BoardState();
        this.gameStatus = EnumGameStatus.CREATED;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GameMove> getHistory() {
        return history;
    }

    public void setHistory(List<GameMove> history) {
        this.history = history;
    }

    public List<GameMove> getLegalMoves() {
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

    public EnumGameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(EnumGameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
