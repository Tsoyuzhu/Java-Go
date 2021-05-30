package com.tsoyuzhu.go.service;

import com.tsoyuzhu.go.domain.api.EnumGameMoveResponseType;
import com.tsoyuzhu.go.domain.api.GameMoveRequest;
import com.tsoyuzhu.go.domain.api.GameMoveResponse;
import com.tsoyuzhu.go.domain.gameLogic.*;
import com.tsoyuzhu.go.repository.GoGameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GoGameServiceTest {

    @Mock
    GoGameRepository goGameRepository;

    @InjectMocks
    GoGameService goGameService;

    @Test
    public void createGame() {
        ArgumentCaptor<GoGame> goGameArgumentCaptor = ArgumentCaptor.forClass(GoGame.class);
        when(goGameRepository.createGoGame(any())).thenReturn(new GoGame());
        goGameService.createGame();
        verify(goGameRepository).createGoGame(goGameArgumentCaptor.capture());
        GoGame goGame = goGameArgumentCaptor.getValue();
        assertEquals(EnumPlayer.BLACK, goGame.getBoardState().getPlayerToMove());
    }

    @Test
    public void updateGame() throws Exception {
        ArgumentCaptor<GoGame> goGameArgumentCaptor = ArgumentCaptor.forClass(GoGame.class);
        when(goGameRepository.findGoGameById(any())).thenReturn(Optional.of(new GoGame()));
        GameMoveRequest request = getTestGameMoveRequest(EnumPlayer.BLACK);
        request.setGameId("someGameId");
        GameMoveResponse response = goGameService.updateGame(request);
        verify(goGameRepository).updateGoGame(goGameArgumentCaptor.capture());
        GoGame goGame = goGameArgumentCaptor.getValue();

        // Check response
        assertNull(response.getDetails());
        assertEquals(EnumGameMoveResponseType.SUCCESSFUL, response.getGameMoveResponseType());

        // Check gameState modified correctly by move request
        assertEquals(EnumPositionState.BLACK, ReflectionTestUtils.invokeMethod(goGame.getBoardState(),"getPositionState", request.getGameMove().getPosition()));
        assertEquals(1, goGame.getHistory().size());
    }

    @Test
    public void updateGameFailsWhenGameDoesntExist() throws Exception {
        when(goGameRepository.findGoGameById(any())).thenReturn(Optional.empty());
        GameMoveRequest request = getTestGameMoveRequest(EnumPlayer.BLACK);
        request.setGameId("someNonExistentGameId");
        GameMoveResponse response = goGameService.updateGame(request);
        assertEquals(EnumGameMoveResponseType.FAILED, response.getGameMoveResponseType());
        assertEquals("GameMove invalid - Game does not exist with requested gameId", response.getDetails());
    }

    @Test
    public void updateGameWithDoublePassEndsGame() {

    }

    private GameMoveRequest getTestGameMoveRequest(EnumPlayer player) {
        GameMoveRequest gameMoveRequest = new GameMoveRequest();
        GameMove gameMove = new GameMove();
        gameMove.setPosition(new Position(18,18));
        gameMove.setPlayer(player);
        gameMove.setMoveType(EnumMoveType.PLACE);
        gameMoveRequest.setGameMove(gameMove);
        return gameMoveRequest;
    }
}