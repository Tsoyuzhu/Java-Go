package com.tsoyuzhu.go.domain.gameLogic;

public enum EnumPositionState {
    WHITE("white"),
    BLACK("black"),
    UNOCCUPIED("unoccupied");

    private String state;

    EnumPositionState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isBlack() {
        return state.equals("black");
    }

    public boolean isWhite() {
        return state.equals("white");
    }

    public boolean isUnoccupied() {
        return state.equals("unoccupied");
    }
}
