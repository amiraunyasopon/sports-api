package com.amir.sports.controllers;

import com.amir.sports.domain.dto.PlayerDto;
import com.amir.sports.domain.entities.PlayerEntity;
import com.amir.sports.mappers.Mapper;
import com.amir.sports.services.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PlayerController {

    private Mapper<PlayerEntity, PlayerDto> playerMapper;

    private PlayerService playerService;

    public PlayerController(Mapper<PlayerEntity, PlayerDto> playerMapper, PlayerService playerService) {
        this.playerMapper = playerMapper;
        this.playerService = playerService;
    }

    @PutMapping(path = "/players/{id}") // design decision to make this both a create and update create(201) update(200)
    public ResponseEntity<PlayerDto> createUpdatePlayer(@PathVariable("id") Long id, @RequestBody PlayerDto playerDto) {
        PlayerEntity playerEntity = playerMapper.mapFrom(playerDto);
        boolean playerExists = playerService.isExists(id);
        PlayerEntity savedPlayerEntity = playerService.createUpdatePlayer(id, playerEntity);
        PlayerDto savedPlayerDto = playerMapper.mapTo(savedPlayerEntity);

        if(playerExists) {
            return new ResponseEntity<>(savedPlayerDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(savedPlayerDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/players")
    public Page<PlayerDto> listPlayers(Pageable pageable) {
        Page<PlayerEntity> players = playerService.findAll(pageable);
        return players.map(playerMapper::mapTo);
    }

    @GetMapping(path = "/players/{id}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable("id") Long id) {
        Optional<PlayerEntity> foundPlayer = playerService.findOne(id);
        return foundPlayer.map(playerEntity -> {
            PlayerDto playerDto = playerMapper.mapTo(playerEntity);
            return new ResponseEntity<>(playerDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/players/{id}")
    public ResponseEntity<PlayerDto> partialUpdatePlayer(@PathVariable("id") Long id, @RequestBody PlayerDto playerDto) {
        if(!playerService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PlayerEntity playerEntity = playerMapper.mapFrom(playerDto);
        PlayerEntity updatedPlayerEntity = playerService.partialUpdate(id, playerEntity);
        return new ResponseEntity<>(playerMapper.mapTo(updatedPlayerEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/players/{id}")
    public ResponseEntity<PlayerDto> deleteBook(@PathVariable("id") Long id) {
        playerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }
}
