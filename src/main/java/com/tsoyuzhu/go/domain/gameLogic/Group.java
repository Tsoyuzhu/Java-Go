package com.tsoyuzhu.go.domain.gameLogic;

import java.util.ArrayList;
import java.util.List;

public class Group {
    // A group in Go is a collection of adjacent pieces of the same time
    private EnumPositionState type;
    private List<Position> positionList;
    private List<Position> libertiesList;

    public Group(EnumPositionState type) {
        this.type = type;
        this.positionList = new ArrayList<>();
    }

    public EnumPositionState getType() {
        return type;
    }

    public void setType(EnumPositionState type) {
        this.type = type;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public List<Position> getLibertiesList() {
        return libertiesList;
    }

    public void setLibertiesList(List<Position> libertiesList) {
        this.libertiesList = libertiesList;
    }

    public boolean isCaptured() {
        // A group is considered captured when it has no remaining liberties
        return libertiesList.isEmpty();
    }
}

