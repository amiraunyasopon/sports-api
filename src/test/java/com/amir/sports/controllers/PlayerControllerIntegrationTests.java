package com.amir.sports.controllers;

import com.amir.sports.TestDataUtil;
import com.amir.sports.domain.dto.PlayerDto;
import com.amir.sports.domain.entities.PlayerEntity;
import com.amir.sports.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class PlayerControllerIntegrationTests {

    private PlayerService playerService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public PlayerControllerIntegrationTests(MockMvc mockMvc, PlayerService playerService) {
        this.playerService = playerService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }
    @Test
    public void testThatCreatePlayerReturnsHttpStatus201Created() throws Exception {
        PlayerDto playerDto = TestDataUtil.createTestPlayerDtoA(null);
        String createPlayerJson = objectMapper.writeValueAsString(playerDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/players/" + playerDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPlayerJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatUpdatePlayerReturnsHttpStatus200Ok() throws Exception {
        PlayerEntity testPlayerEntityA = TestDataUtil.createTestPlayerEntityA(null);
        PlayerEntity savedPlayerEntity = playerService.createUpdatePlayer(testPlayerEntityA.getId(), testPlayerEntityA);

        PlayerDto playerDto = TestDataUtil.createTestPlayerDtoA(null);
        testPlayerEntityA.setId(savedPlayerEntity.getId());
        String createPlayerJson = objectMapper.writeValueAsString(playerDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/players/" + savedPlayerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPlayerJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatCreatePlayerReturnsCreatedPlayer() throws Exception {
        PlayerDto playerDto = TestDataUtil.createTestPlayerDtoA(null);
        String createPlayerJson = objectMapper.writeValueAsString(playerDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/players/" + playerDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPlayerJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(4700L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Yi Sols")
        );
    }

    @Test
    public void testThatUpdatePlayerReturnsUpdatedPlayer() throws Exception {
        //save player in database
        PlayerEntity testPlayerEntityA = TestDataUtil.createTestPlayerEntityA(null);
        PlayerEntity savedPlayerEntity = playerService.createUpdatePlayer(testPlayerEntityA.getId(), testPlayerEntityA);
        //run update
        PlayerDto testPlayerDtoA = TestDataUtil.createTestPlayerDtoA(null);
        testPlayerDtoA.setName("UPDATED");
        testPlayerDtoA.setId(savedPlayerEntity.getId());
        String createPlayerJson = objectMapper.writeValueAsString(testPlayerDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/players/" + savedPlayerEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPlayerJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(4700L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        );
    }

    @Test
    public void testThatListPlayersReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/players")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListPlayersReturnsListOfPlayers() throws Exception {
        PlayerEntity testPlayerEntityA = TestDataUtil.createTestPlayerEntityA(null);
        playerService.createUpdatePlayer(testPlayerEntityA.getId(), testPlayerEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/players")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").value(4700L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].name").value("Yi Sols")
        );
    }

    @Test
    public void testThatGetPlayerReturnsHttpStatus200OkWhenPlayerExists() throws Exception {
        PlayerEntity testPlayerEntityA = TestDataUtil.createTestPlayerEntityA(null);
        playerService.createUpdatePlayer(testPlayerEntityA.getId(), testPlayerEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/players/" + testPlayerEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetPlayerReturnsHttpStatus404WhenNoPlayerExists() throws Exception {
        PlayerEntity testPlayerEntityA = TestDataUtil.createTestPlayerEntityA(null);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/players/" + testPlayerEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetPlayerReturnsPlayerWhenPlayerExists() throws Exception {
        PlayerEntity testPlayerEntityA = TestDataUtil.createTestPlayerEntityA(null);
        playerService.createUpdatePlayer(testPlayerEntityA.getId(), testPlayerEntityA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/players/" + testPlayerEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(4700L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Yi Sols")
        );
    }
}
