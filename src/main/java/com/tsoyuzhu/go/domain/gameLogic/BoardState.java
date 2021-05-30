package com.tsoyuzhu.go.domain.gameLogic;

import java.util.*;
import java.util.stream.Collectors;

public class BoardState {

    private static final int SIZE = 19;

    private static final EnumPlayer GO_STARTING_PLAYER = EnumPlayer.BLACK;

    // Player who is making a move this turn
    private EnumPlayer playerToMove;

    // If we never exceed initial capacity then performance does not suffer from using an arrayList
    private List<EnumPositionState> positionStates = new ArrayList<EnumPositionState>(SIZE*SIZE);

    // Keep a copy of the last state to check whether the KO rule has been broken
    private List<EnumPositionState> previousState = new ArrayList<>(SIZE*SIZE);

    // Track if last player has passed
    private boolean previousPlayerPassed;

    // Detemrine if game as ended
    private boolean gameComplete;

    public BoardState() {
        // Init board with empty states
        for (int i = 0; i < SIZE*SIZE; i++) {
            positionStates.add(EnumPositionState.UNOCCUPIED);
            previousState.add(EnumPositionState.UNOCCUPIED);
        }
        playerToMove = GO_STARTING_PLAYER;
        gameComplete = false;
        previousPlayerPassed = false;
    }

    public void handleMoveRequest(GameMove gameMove) throws Exception {
        verifyGameMove(gameMove);
        if (gameMove.getMoveType().equals(EnumMoveType.PASS)) {
            if (this.previousPlayerPassed) {
                // Game ends if both players pass
                this.gameComplete = true;
            } else {
                this.previousPlayerPassed = true;
            }
        } else {
            // Set the new previous state as the current
            previousState = new ArrayList<>(positionStates);
            // Move is legal so we can make the move and advance the state
            setPositionState(gameMove.getPosition(), gameMove.getPieceType());
            advanceState(gameMove);
            this.previousPlayerPassed = false;
        }
        playerToMove = (playerToMove == EnumPlayer.BLACK) ? EnumPlayer.WHITE : EnumPlayer.BLACK;
    }

    private boolean isMoveLegal(GameMove gameMove) {
        if (gameMove.getMoveType().equals(EnumMoveType.PLACE)) {
            return isValidPosition(gameMove.getPosition()) && moveNotSelfCaptureOrKO(gameMove);
        }
        return true;
    }

    private void advanceState(GameMove gameMove) {
        // Remove all captured groups by setting the position in each captured group to UNOCCUPIED
        List<Group> groups = getGroups(gameMove);

        // However, the group which contains the most recent move is immune to capture if the move resulted in a capture
        // We can determine this by checking if any of the target capture groups have 0 liberties
        boolean latestMoveGroupImmune = groups.stream().anyMatch(g -> g.isLibertyOccupiedByLatestGameMove() && !g.isGroupOfLatestMove() && g.getLibertiesList().size() == 0);
        groups.stream()
                .filter(g -> g.isCaptured() && !g.isGroupOfLatestMove())
                .forEach(g -> {
                    g.getPositionList().forEach(p -> setPositionState(p, EnumPositionState.UNOCCUPIED));
                });

        // If the group is not immune and has no liberties then we need to set it's positions to captured
        if (!latestMoveGroupImmune) {
            Optional<Group> latestMoveGroup = groups.stream()
                    .filter(g -> g.isGroupOfLatestMove() && g.getLibertiesList().size() == 0)
                    .findFirst();

            latestMoveGroup.ifPresent(g -> g.getPositionList().forEach(p -> setPositionState(p, EnumPositionState.UNOCCUPIED)));
        }
    }

    public void setPositionStates(List<EnumPositionState> positionStates) {
        this.positionStates = positionStates;
    }

    public EnumPlayer getPlayerToMove() {
        return playerToMove;
    }

    public void setPlayerToMove(EnumPlayer playerToMove) {
        this.playerToMove = playerToMove;
    }

    public boolean isGameComplete() {
        return gameComplete;
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
        return positionInbounds(position) && positionIsUnoccupied(position);
    }

    private boolean positionIsUnoccupied(Position position) {
        return getPositionState(position).isUnoccupied();
    }

    private boolean positionInbounds(Position position) {
        return position.getX() < SIZE && position.getY() < SIZE;
    }

    // This check requires an expensive copy operation of board state. Check self capture and KO at the same time to improve performance.
    private boolean moveNotSelfCaptureOrKO(GameMove gameMove) {
        // Simulate the play and then determine whether this results in the piece becoming captured OR resulting in a KO breach
        boolean returnValue = false;
        // This copy operation is expensive. Needs improvement.
        List<EnumPositionState> positionStatesCopy = new ArrayList<>(positionStates);
        setPositionState(gameMove.getPosition(),gameMove.getPieceType());
        advanceState(gameMove);

        if (!getPositionState(gameMove.getPosition()).isUnoccupied() && !positionStates.equals(previousState)) {
            returnValue = true;
        }
        // Preserve the board state
        positionStates = positionStatesCopy;
        return returnValue;
    }

    private List<Group> getGroups(GameMove latestMove) {
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
                    // We have a new group! Begin searching for other group members via DFS
                    Group group = new Group(type);
                    dfs(position,visited,group,latestMove);
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    private void dfs(Position position, List<Position> visited, Group group, GameMove latestMove) {
        // This dfs has three goals:
        // The first is to identify potential group members.
        // The second is to identify if the latest game move has resulted in a capture.
        // The third is to identify the group which contains the latest game move.
        if (!visited.contains(position)) {
            if (getPositionState(position).isUnoccupied()) {
                // Found liberty
                group.getLibertiesList().add(position);
            } else if (getPositionState(position).equals(group.getType())) {
                // Found group member
                group.getPositionList().add(position);
                // Found group of the latest Move
                if (getPositionState(position).equals(latestMove.getPieceType()) && position.equals(latestMove.getPosition())) {
                    group.setGroupOfLatestMove(true);
                }
                visited.add(position);
                // Call dfs on neighbours to find more
                List<Position> neighbours = position.getSurroundPositions().stream()
                        .filter(this::positionInbounds)
                        .collect(Collectors.toList());
                neighbours.forEach(n -> dfs(n,visited,group,latestMove));
            }
        }
        // Otherwise, check for a liberty which is occupied by the latest game move
        if (position.equals(latestMove.getPosition()) && group.getType() != latestMove.getPieceType()) {
            // Found group which has one of it's liberties occupied by latest gameMove
            group.setLibertyOccupiedByLatestGameMove(true);
        }
    }

    public void verifyGameMove(GameMove gameMove) throws Exception {
        if (!gameMove.getPlayer().equals(playerToMove)) {
            throw new Exception("Invalid move - It is " + playerToMove.toString() + "'s turn to play");
        }
        if (!isMoveLegal(gameMove)) {
            throw new Exception("Invalid move - Illegal");
        }
    }

}
