package com.tsoyuzhu.go.domain.gameLogic;

public class GameMove {
    private EnumMoveType moveType;
    private EnumPlayer player;
    private Position position;

    public EnumMoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(EnumMoveType moveType) {
        this.moveType = moveType;
    }

    public EnumPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EnumPlayer player) {
        this.player = player;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public EnumPositionState getPieceType() {
        switch(player) {
            case BLACK:
                return EnumPositionState.BLACK;
            case WHITE:
                return EnumPositionState.WHITE;
            default:
                return null;
        }
    }
}
