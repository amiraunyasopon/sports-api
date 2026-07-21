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
    public ResponseEntity<PlayerDto> getBook(@PathVariable("id") Long id) {
        Optional<PlayerEntity> foundBook = playerService.findOne(id);
        return foundBook.map(bookEntity -> {
            PlayerDto bookDto = playerMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
