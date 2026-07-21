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
}
