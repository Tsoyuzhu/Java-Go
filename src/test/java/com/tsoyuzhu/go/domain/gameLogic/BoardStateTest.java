package com.tsoyuzhu.go.domain.gameLogic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
public class BoardStateTest {

    @Test
    public void outOfBoundsMoveIsIllegal() throws IOException {
        BoardState boardState = GameLogicTestUtils.readBoard("board_state_1.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.BLACK);
        Position outOfBoundsPosition = new Position(19, 19);
        move.setPosition(outOfBoundsPosition);
        assertFalse(boardState.isMoveLegal(move));
    }

    @Test
    public void playOnExistingPieceIsIllegal() throws IOException {
        BoardState boardState = GameLogicTestUtils.readBoard("board_state_1.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.BLACK);
        Position occupiedPosition = new Position(5, 3);
        move.setPosition(occupiedPosition);
        assertFalse(boardState.isMoveLegal(move));
    }

    @Test
    public void playSelfCaptureIsIllegal() throws IOException {
        BoardState boardState = GameLogicTestUtils.readBoard("board_state_2.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.BLACK);
        Position selfCapturePosition = new Position(18, 18);
        move.setPosition(selfCapturePosition);
        assertFalse(boardState.isMoveLegal(move));
    }

    @Test
    public void playSelfCaptureAdjacentOwnPieceIsIllegal() throws IOException {
        BoardState boardState = GameLogicTestUtils.readBoard("board_state_2.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.WHITE);
        Position selfCapturePosition = new Position(4, 13);
        move.setPosition(selfCapturePosition);
        assertFalse(boardState.isMoveLegal(move));
    }

    @Test
    public void playSurroundOwnPieceIsLegal() throws IOException {
        BoardState boardState = GameLogicTestUtils.readBoard("board_state_2.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.WHITE);
        Position selfSurroundPosition = new Position(18, 18);
        move.setPosition(selfSurroundPosition);
        assertTrue(boardState.isMoveLegal(move));
    }

    @Test
    public void playSelfCaptureResultingInCaptureIsLegal() throws IOException {
        BoardState boardState = GameLogicTestUtils.readBoard("board_state_3.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.BLACK);
        Position capturePosition = new Position(7,12);
        move.setPosition(capturePosition);
        assertTrue(boardState.isMoveLegal(move));
    }
}