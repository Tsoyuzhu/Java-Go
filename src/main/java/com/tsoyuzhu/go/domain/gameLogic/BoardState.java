package com.tsoyuzhu.go.domain.gameLogic;

import java.util.*;
import java.util.stream.Collectors;

public class BoardState {

    private static final int SIZE = 19;

    // If we never exceed initial capacity then performance does not suffer from using an arrayList
    private List<EnumPositionState> positionStates = new ArrayList<EnumPositionState>(SIZE*SIZE);

    // Keep a copy of the last state to check whether the KO rule has been broken
    private List<EnumPositionState> previousState = new ArrayList<>(SIZE*SIZE);

    public BoardState() {
        // Init board with empty states
        for (int i = 0; i < SIZE*SIZE; i++) {
            positionStates.add(EnumPositionState.UNOCCUPIED);
            previousState.add(EnumPositionState.UNOCCUPIED);
        }
    }

    public boolean isMoveLegal(BoardState boardState, GameMove gameMove) {
        return isValidPosition(gameMove.getPosition()) && moveNotSelfCaptureOrKO(gameMove);
    }

    private void advanceState() {
        // Remove all captured groups by setting the position in each captured group to UNOCCUPIED
        List<Group> groups = getGroups();
        groups.stream()
                .filter(Group::isCaptured)
                .forEach(g -> {
                    g.getPositionList().forEach(p -> setPositionState(p, EnumPositionState.UNOCCUPIED));
                });
    }

    public void setPositionStates(List<EnumPositionState> positionStates) {
        this.positionStates = positionStates;
    }

    public void setPreviousState(List<EnumPositionState> previousState) {
        this.previousState = previousState;
    }

    // HELPERS
    private EnumPositionState getPositionState(Position position) {
        // We can calculate the position like this to keep the memory contiguous for performance optimisation
        return positionStates.get(SIZE * position.getY() + position.getX());
    }

    private void setPositionState(Position position, EnumPositionState state) {
        positionStates.set(SIZE * position.getY() + position.getX(), state);
    }

    private boolean isValidPosition(Position position) {
        // Position not occupied and exists on board
        return position.getX() < SIZE
                && position.getY() < SIZE
                && getPositionState(position).isUnoccupied();
    }

    // This check requires an expensive copy operation of board state. Check self capture and KO at the same time to improve performance.
    private boolean moveNotSelfCaptureOrKO(GameMove gameMove) {
        // Simulate the play and then determine whether this results in the piece becoming captured OR resulting in a KO breach
        boolean returnValue = false;
        // This copy operation is expensive. Needs improvement.
        List<EnumPositionState> positionStatesCopy = new ArrayList<>(positionStates);
        advanceState();
        if (getPositionState(gameMove.getPosition()).isUnoccupied() && !positionStates.equals(previousState)) {
            returnValue = true;
        }
        // Preserve the board state
        positionStates = positionStatesCopy;
        return returnValue;
    }

    private List<Group> getGroups() {
        // Track indices visited
        List<Position> visited = new ArrayList<>();

        // Track existing groups
        List<Group> groups = new ArrayList<>();

        // Sweep through the board
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                Position position = new Position(x,y);
                // Current piece
                EnumPositionState type = getPositionState(position);
                if (!type.isUnoccupied() && !visited.contains(position) ) {
                    // We have a new group! Begin searching for other group members via BFS
                    Group group = new Group(type);

                    // Track the neighbouring positions we need to visit in the BFS and initialise the queue
                    Queue<Position> toVisit = new LinkedList<>(Collections.singletonList(position));

                    while (!toVisit.isEmpty()) {
                        Position navigator = toVisit.remove();
                        // If we have already visited this element skip it
                        if (visited.contains(navigator)) {
                            continue;
                        }
                        if ( getPositionState(navigator).isUnoccupied() ) {
                            // If it is empty, then it must be a liberty of this group! Add it to the liberty set
                            group.getLibertiesList().add(navigator);
                            // Do not mark the element as visited because liberties can be shared between groups
                        } else if (getPositionState(navigator).equals(type)) {
                            // If the piece is the same type then it must belong to the group
                            group.getPositionList().add(navigator);
                            // Add it's neighbours to the toVisit queue if they haven't already been visited
                            List<Position> neighbours = navigator.getSurroundPositions().stream().filter(p -> !visited.contains(p)).collect(Collectors.toList());
                            toVisit.addAll(neighbours);
                            visited.add(navigator);
                        }
                    }
                    groups.add(group);
                }
            }
        }
        return groups;
    }
}
