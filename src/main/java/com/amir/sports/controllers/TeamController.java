package com.amir.sports.controllers;

import com.amir.sports.domain.dto.TeamDto;
import com.amir.sports.domain.entities.TeamEntity;
import com.amir.sports.mappers.Mapper;
import com.amir.sports.services.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController {

    private TeamService teamService;

    private Mapper<TeamEntity, TeamDto> teamMapper;

    public TeamController(TeamService teamService, Mapper<TeamEntity, TeamDto> teamMapper)
    {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
    }

    @PostMapping(path = "/teams")
    public ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto team)
    {
        TeamEntity teamEntity = teamMapper.mapFrom(team);
        TeamEntity savedTeamEntity = teamService.save(teamEntity);
        return new ResponseEntity<>(teamMapper.mapTo(savedTeamEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/teams")
    public Page<TeamDto> listTeams(Pageable pageable)
    {
        Page<TeamEntity> teams = teamService.findAll(pageable);
        return teams.map(teamMapper::mapTo);
    }
}
