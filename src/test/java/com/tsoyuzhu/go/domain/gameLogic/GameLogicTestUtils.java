package com.tsoyuzhu.go.domain.gameLogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GameLogicTestUtils {
    private static final int EOF = -1;
    private static final char BLACK_PIECE = 'X';
    private static final char WHITE_PIECE = 'O';
    private static final char UNOCCUPIED = '.';
    private static final char NEWLINE = '\n';

    private static final Logger LOG = LoggerFactory.getLogger(GameLogicTestUtils.class);

    public static BoardState readBoard(String filename) throws IOException {
        // Generate a boardState from a given text file to help generate test scenarios
        File file = new File(String.format("src/test/resources/gameScenarios/%s",filename));
        InputStream input = new FileInputStream(file);
        Reader reader = new InputStreamReader(input, Charset.defaultCharset());
        int r, index;
        BoardState boardState = new BoardState();
        List<EnumPositionState> positionStates = new ArrayList<>();

        while((r = reader.read()) != EOF ){
            // Attempt cast
            char ch = (char) r;
            EnumPositionState state = null;
            switch (ch) {
                case BLACK_PIECE:
                    state = EnumPositionState.BLACK;
                    break;
                case WHITE_PIECE:
                    state = EnumPositionState.WHITE;
                    break;
                case UNOCCUPIED:
                    state = EnumPositionState.UNOCCUPIED;
                    break;
                case NEWLINE:
                    continue;
                default:
                    LOG.info("Read unknown character \"{}\". Cannot convert to boardstate", ch);
            }
            if (state != null) {
                positionStates.add(state);
            }
        }
        boardState.setPositionStates(positionStates);
        return boardState;
    }

    public static GameMove getMove(EnumPlayer player) {
        GameMove move = new GameMove();
        move.setPlayer(player);
        move.setMoveType(EnumMoveType.PLACE);
        return move;
    }
}
