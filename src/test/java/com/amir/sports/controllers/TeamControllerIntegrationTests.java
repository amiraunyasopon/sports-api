package com.amir.sports.controllers;

import com.amir.sports.TestDataUtil;
import com.amir.sports.domain.dto.TeamDto;
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

    @Test
    public void testThatGetTeamsReturnsHttpStatus200WhenTeamExists() throws Exception {
        TeamEntity testTeamEntityA = TestDataUtil.createTestTeamEntityA();
        teamService.save(testTeamEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetTeamsReturnsHttpStatus404WhenNoTeamExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/teams/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetTeamsReturnsTeamWhenTeamExists() throws Exception {
        TeamEntity testTeamEntityA = TestDataUtil.createTestTeamEntityA();
        teamService.save(testTeamEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Thailand Tigers")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.winLossRatio").value(0.54)
        );
    }

    @Test
    public void testThatFullUpdateTeamReturnsHttpStatus420WhenNoTeamExists() throws Exception {
        TeamDto testTeamDtoA = TestDataUtil.createTestTeamDtoA();
        String TeamDtoJson = objectMapper.writeValueAsString(testTeamDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/teams/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TeamDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateTeamReturnsHttpStatus200WhenTeamExists() throws Exception {
        TeamEntity testTeamEntityA = TestDataUtil.createTestTeamEntityA();
        TeamEntity savedTeam = teamService.save(testTeamEntityA);

        TeamDto testTeamDtoA = TestDataUtil.createTestTeamDtoA();
        String teamDtoJson = objectMapper.writeValueAsString(testTeamDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/teams/" + savedTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teamDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingTeam() throws Exception {
        TeamEntity testTeamEntityA = TestDataUtil.createTestTeamEntityA();
        TeamEntity savedTeam = teamService.save(testTeamEntityA);

        TeamEntity teamDto = TestDataUtil.createTestTeamEntityB();
        teamDto.setId(savedTeam.getId());

        String teamDtoUpdateJson = objectMapper.writeValueAsString(teamDto);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/teams/" + savedTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teamDtoUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedTeam.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(teamDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.winLossRatio").value(teamDto.getWinLossRatio())
        );
    }

    @Test
    public void testThatPartialUpdateExistingTeamReturnsHttpStatus200Ok() throws Exception{
        TeamEntity testTeamEntityA = TestDataUtil.createTestTeamEntityA();
        TeamEntity savedTeam = teamService.save(testTeamEntityA);

        TeamDto testTeamDtoA = TestDataUtil.createTestTeamDtoA();
        testTeamDtoA.setName("UPDATED");
        String teamDtoJson = objectMapper.writeValueAsString(testTeamDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/teams/" + savedTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teamDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateExistingTeamReturnsUpdatedTeam() throws Exception{
        TeamEntity testTeamEntityA = TestDataUtil.createTestTeamEntityA();
        TeamEntity savedTeam = teamService.save(testTeamEntityA);

        TeamDto testTeamDtoA = TestDataUtil.createTestTeamDtoA();
        testTeamDtoA.setName("UPDATED");
        String teamDtoJson = objectMapper.writeValueAsString(testTeamDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/teams/" + savedTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teamDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedTeam.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.winLossRatio").value(testTeamDtoA.getWinLossRatio())
        );
    }

    @Test
    public void testThatDeleteTeamReturnsHttpStatus204ForNonExistingTeam() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/teams/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteTeamReturnsHttpStatus204ForExistingTeam() throws Exception {
        TeamEntity testTeamEntityA = TestDataUtil.createTestTeamEntityA();
        TeamEntity savedTeam = teamService.save(testTeamEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/teams/" + savedTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
