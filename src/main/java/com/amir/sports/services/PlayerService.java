package com.amir.sports.services;

import com.amir.sports.domain.entities.PlayerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PlayerService {
    PlayerEntity save(PlayerEntity playerEntity);

    PlayerEntity createUpdatePlayer(Long id, PlayerEntity playerEntity);

    Page<PlayerEntity> findAll(Pageable pageable);

    Page<PlayerEntity> search(String name, String position, String teamName, Pageable pageable);

    Optional<PlayerEntity> findOne(Long id);

    boolean isExists(Long id);

    PlayerEntity partialUpdate(Long id, PlayerEntity playerEntity);

    void delete(Long id);
}
