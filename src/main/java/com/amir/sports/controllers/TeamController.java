package com.amir.sports.controllers;

import com.amir.sports.domain.dto.TeamDto;
import com.amir.sports.domain.entities.TeamEntity;
import com.amir.sports.mappers.Mapper;
import com.amir.sports.services.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @GetMapping(path = "/teams/{id}")
    public ResponseEntity<TeamDto> getTeam(@PathVariable("id") Long id)
    {
        Optional<TeamEntity> foundTeam = teamService.findOne(id);
        return foundTeam.map(teamEntity -> {
            TeamDto teamDto = teamMapper.mapTo(teamEntity);
            return new ResponseEntity<>(teamDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/teams/{id}")
    public ResponseEntity<TeamDto> fullUpdateTeam(@PathVariable("id") Long id, @RequestBody TeamDto teamDto) {
        if(!teamService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        teamDto.setId(id); // overwrite id in body with path id
        TeamEntity teamEntity = teamMapper.mapFrom(teamDto);

        TeamEntity savedTeamEntity = teamService.save(teamEntity);
        return new ResponseEntity<>(
                teamMapper.mapTo(savedTeamEntity),
                HttpStatus.OK
        );
    }

    @PatchMapping(path = "/teams/{id}")
    public ResponseEntity<TeamDto> partialUpdate(@PathVariable("id") Long id, @RequestBody TeamDto teamDto) {
        if(!teamService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TeamEntity teamEntity = teamMapper.mapFrom(teamDto);
        TeamEntity savedTeamEntity = teamService.partialUpdate(id, teamEntity);
        return new ResponseEntity<>(
                teamMapper.mapTo(savedTeamEntity),
                HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/teams/{id}")
    public ResponseEntity<TeamDto> deleteAuthor(@PathVariable("id") Long id) {
        teamService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }
}
