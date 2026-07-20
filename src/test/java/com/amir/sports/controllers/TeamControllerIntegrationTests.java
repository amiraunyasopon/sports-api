package com.amir.sports.controllers;

import com.amir.sports.TestDataUtil;
import com.amir.sports.domain.entities.TeamEntity;
import com.amir.sports.services.TeamService;
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
public class TeamControllerIntegrationTests {
    
    private TeamService teamService;
    
    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper;
    
    @Autowired
    public TeamControllerIntegrationTests(MockMvc mockMvc, TeamService teamService)
    {
        this.mockMvc = mockMvc;
        this.teamService = teamService;
        this.objectMapper = new ObjectMapper();
    }
    
    @Test
    public void testThatCreateTeamSuccessfullyReturnsHttp201Created() throws Exception{
        TeamEntity testTeamA = TestDataUtil.createTestTeamEntityA();
        testTeamA.setId(null);
        String teamJson = objectMapper.writeValueAsString(testTeamA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teamJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateTeamSuccessfullyReturnsSavedTeam() throws Exception{
        TeamEntity testTeamA = TestDataUtil.createTestTeamEntityA();
        testTeamA.setId(null);
        String teamJson = objectMapper.writeValueAsString(testTeamA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teamJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Thailand Tigers")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.winLossRatio").value(0.54)
        );
    }

    @Test
    public void testThatListTeamsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListTeamsReturnsListOfTeams() throws Exception {
        TeamEntity testTeamEntityA = TestDataUtil.createTestTeamEntityA();
        teamService.save(testTeamEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].name").value("Thailand Tigers")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].winLossRatio").value(0.54)
        );
    }
}
