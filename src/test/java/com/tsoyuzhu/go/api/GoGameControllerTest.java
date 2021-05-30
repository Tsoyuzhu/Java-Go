package com.tsoyuzhu.go.api;


import com.tsoyuzhu.go.domain.api.EnumGameMoveResponseType;
import com.tsoyuzhu.go.domain.api.GameMoveResponse;
import com.tsoyuzhu.go.domain.gameLogic.GoGame;
import com.tsoyuzhu.go.service.GoGameService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(GoGameController.class)
public class GoGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoGameService goGameService;

    private String goGameId;

    @Before
    public void init() {
        this.goGameId = "someGoGameId";
        initMocks();
    }

    @Test
    public void createGoGame() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/game/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()));
    }

    @Test
    public void updateGoGame() throws Exception {
        GameMoveResponse gameMoveResponse = new GameMoveResponse(goGameId);
        gameMoveResponse.setGameMoveResponseType(EnumGameMoveResponseType.SUCCESSFUL);
        gameMoveResponse.setDetails(null);
        when(goGameService.updateGame(any())).thenReturn(gameMoveResponse);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/api/v1/game/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gameId\": \"someGoGameId\", \"gameMove\": { \"moveType\": \"PLACE\", \"player\": \"BLACK\", \"position\": {\"x\": 18, \"y\": 18} } }"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameMoveResponseType", Matchers.is(EnumGameMoveResponseType.SUCCESSFUL.toString())));
    }

    @Test
    public void updateGoGameInvalidMoveRequest() throws Exception {
        GameMoveResponse gameMoveResponse = new GameMoveResponse(goGameId);
        gameMoveResponse.setGameMoveResponseType(EnumGameMoveResponseType.FAILED);
        gameMoveResponse.setDetails("GameMove Invalid - gameId was not provided in request");
        when(goGameService.updateGame(any())).thenReturn(gameMoveResponse);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/api/v1/game/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gameMove\": { \"moveType\": \"PLACE\", \"player\": \"BLACK\", \"position\": {\"x\": 18, \"y\": 18} } }"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameMoveResponseType", Matchers.is(EnumGameMoveResponseType.FAILED.toString())));
    }

    @Test
    public void deleteGoGame() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/api/v1/game/"+this.goGameId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.is("Delete successful")));
    }

    @Test
    public void deleteGoGameInvalidGameId() throws Exception {
        String errorMessage = "GoGame with id " + this.goGameId + " does not exist - Cannot delete";
        doThrow(new Exception(errorMessage)).when(goGameService).deleteGame(any());
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/api/v1/game/"+this.goGameId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.is(errorMessage)));
    }

    private void initMocks() {
        GoGame goGame = new GoGame();
        goGame.setId(this.goGameId);
        when(goGameService.createGame()).thenReturn(goGame);
    }
}