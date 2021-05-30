package com.tsoyuzhu.go.domain.gameLogic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class GoGameTest {

    @Test
    public void outOfBoundsMoveIsIllegal() throws IOException {
        GoGame goGame = GameLogicTestUtils.readBoard("board_state_1.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.BLACK);
        Position outOfBoundsPosition = new Position(19, 19);
        move.setPosition(outOfBoundsPosition);
        assertFalse(ReflectionTestUtils.invokeMethod(goGame,"isMoveLegal",move));
    }

    @Test
    public void playOnExistingPieceIsIllegal() throws IOException {
        GoGame goGame = GameLogicTestUtils.readBoard("board_state_1.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.BLACK);
        Position occupiedPosition = new Position(5, 3);
        move.setPosition(occupiedPosition);
        assertFalse(ReflectionTestUtils.invokeMethod(goGame,"isMoveLegal",move));
    }

    @Test
    public void playSelfCaptureIsIllegal() throws IOException {
        GoGame goGame = GameLogicTestUtils.readBoard("board_state_2.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.BLACK);
        Position selfCapturePosition = new Position(18, 18);
        move.setPosition(selfCapturePosition);
        assertFalse(ReflectionTestUtils.invokeMethod(goGame,"isMoveLegal",move));
    }

    @Test
    public void playSelfCaptureAdjacentOwnPieceIsIllegal() throws IOException {
        GoGame goGame = GameLogicTestUtils.readBoard("board_state_2.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.WHITE);
        Position selfCapturePosition = new Position(4, 13);
        move.setPosition(selfCapturePosition);
        assertFalse(ReflectionTestUtils.invokeMethod(goGame,"isMoveLegal",move));
    }

    @Test
    public void playSurroundOwnPieceIsLegal() throws IOException {
        GoGame goGame = GameLogicTestUtils.readBoard("board_state_2.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.WHITE);
        Position selfSurroundPosition = new Position(18, 18);
        move.setPosition(selfSurroundPosition);
        assertTrue(ReflectionTestUtils.invokeMethod(goGame,"isMoveLegal",move));
    }

    @Test
    public void playSelfCaptureResultingInCaptureIsLegal() throws IOException {
        GoGame goGame = GameLogicTestUtils.readBoard("board_state_3.txt");
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.BLACK);
        Position capturePosition = new Position(7,12);
        move.setPosition(capturePosition);
        assertTrue(ReflectionTestUtils.invokeMethod(goGame,"isMoveLegal",move));
    }

    @Test
    public void playOutOfOrderThrowsException() throws Exception {
        GoGame goGame = GameLogicTestUtils.readBoard("board_state_3.txt");
        goGame.setPlayerToMove(EnumPlayer.BLACK);
        // Try to play as WHITE
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.WHITE);
        Position dummyPosition = new Position(7,12);
        move.setPosition(dummyPosition);
        assertThatThrownBy(() -> goGame.handleMoveRequest(move))
                .isExactlyInstanceOf(Exception.class)
                .hasMessage("Invalid move - It is BLACK's turn to play");
    }

    @Test
    public void playIllegalMoveThrowsException() throws Exception {
        GoGame goGame = GameLogicTestUtils.readBoard("board_state_2.txt");
        goGame.setPlayerToMove(EnumPlayer.WHITE);
        GameMove move = GameLogicTestUtils.getMove(EnumPlayer.WHITE);
        Position illegalMovePosition = new Position(4, 13);
        move.setPosition(illegalMovePosition);
        assertThatThrownBy(() -> goGame.handleMoveRequest(move))
                .isExactlyInstanceOf(Exception.class)
                .hasMessage("Invalid move - Illegal");
    }

    @Test
    public void doublePassEndsGame() throws Exception {
        GoGame goGame = new GoGame();
        GameMove passMove = new GameMove();
        passMove.setMoveType(EnumMoveType.PASS);
        passMove.setPlayer(EnumPlayer.BLACK);
        // First move request to pass
        goGame.handleMoveRequest(passMove);
        // Second move request to pass
        passMove.setPlayer(EnumPlayer.WHITE);
        goGame.handleMoveRequest(passMove);
        assertEquals(EnumGameStatus.COMPLETED, goGame.getGameStatus());
    }

    @Test
    public void passInBetweenDoesNotEndGame() throws Exception {
        GoGame goGame = new GoGame();
        GameMove passMove = new GameMove();
        passMove.setMoveType(EnumMoveType.PASS);
        passMove.setPlayer(EnumPlayer.BLACK);

        GameMove placeMove = new GameMove();
        placeMove.setMoveType(EnumMoveType.PLACE);
        placeMove.setPlayer(EnumPlayer.WHITE);
        placeMove.setPosition(new Position(18,18));

        // First move request to pass
        goGame.handleMoveRequest(passMove);

        // Second move request NOT pass
        goGame.handleMoveRequest(placeMove);
        assertEquals(EnumGameStatus.ONGOING, goGame.getGameStatus());
    }
}