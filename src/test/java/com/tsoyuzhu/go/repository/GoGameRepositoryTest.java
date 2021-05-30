package com.tsoyuzhu.go.repository;

import com.tsoyuzhu.go.domain.gameLogic.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@RunWith(SpringRunner.class)
@ComponentScan("com.tsoyuzhu.go.repository")
public class GoGameRepositoryTest {

    @Autowired
    private GoGameRepository goGameRepository;

    @Test
    public void createGoGameProvidesId() throws IOException {
        GoGame goGame = getTestGoGame();
        goGame.setId(null);
        goGame = goGameRepository.createGoGame(goGame);
        assertNotNull(goGame.getId());
    }

    @Test
    public void findGoGameById() throws IOException {
        goGameRepository.createGoGame(getTestGoGame());
        Optional<GoGame> goGameExists = goGameRepository.findGoGameById("GoGame9090");
        assertTrue(goGameExists.isPresent());
    }

    @Test
    public void updateGoGame() throws IOException {
        GoGame goGame = getTestGoGame();
        goGameRepository.createGoGame(goGame);
        goGame.getHistory().add(getTestGameMove(EnumPlayer.BLACK));
        goGameRepository.updateGoGame(goGame);
        Optional<GoGame> goGameExists = goGameRepository.findGoGameById("GoGame9090");
        assertTrue(goGameExists.isPresent());
        assertTrue(goGameExists.get().getHistory().size() > 0);
    }

    private GoGame getTestGoGame() throws IOException {
        GoGame goGame = new GoGame();
        BoardState boardState = GameLogicTestUtils.readBoard("board_state_1.txt");
        goGame.setId("GoGame9090");
        goGame.setBoardState(boardState);
        return goGame;
    }

    private GameMove getTestGameMove(EnumPlayer player) {
        GameMove gameMove = new GameMove();
        gameMove.setPlayer(player);
        gameMove.setPosition(new Position(18,18));
        gameMove.setMoveType(EnumMoveType.PLACE);
        return gameMove;
    }
}