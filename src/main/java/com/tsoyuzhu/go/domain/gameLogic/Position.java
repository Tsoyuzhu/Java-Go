package com.tsoyuzhu.go.domain.gameLogic;

import java.util.Arrays;
import java.util.List;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position getAbove() {
        return new Position(x,y+1);
    }

    public Position getBelow() {
        return new Position(x,y-1);
    }

    public Position getRight() {
        return new Position(x+1,y);
    }

    public Position getLeft() {
        return new Position(x-1,y);
    }

    public List<Position> getSurroundPositions() {
        return Arrays.asList(getLeft(),getRight(),getAbove(),getBelow());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }
}
