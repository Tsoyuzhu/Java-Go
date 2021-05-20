package com.tsoyuzhu.go.domain.gameLogic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
public class BoardStateTest {

    @Test
    public void isMoveLegal() throws IOException {
        GameLogicTestUtils.readBoard("board_state_1.txt");
    }
}