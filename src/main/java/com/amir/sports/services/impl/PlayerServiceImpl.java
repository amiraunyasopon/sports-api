package com.amir.sports.services.impl;

import com.amir.sports.domain.entities.PlayerEntity;
import com.amir.sports.repositories.PlayerRepository;
import com.amir.sports.services.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {this.playerRepository = playerRepository;}

    @Override
    public PlayerEntity createUpdatePlayer(Long id, PlayerEntity playerEntity) {
        playerEntity.setId(id);
        return playerRepository.save(playerEntity);
    }

    @Override
    public PlayerEntity save(PlayerEntity playerEntity) {
        return playerRepository.save(playerEntity);
    }

    @Override
    public Page<PlayerEntity> findAll(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Override
    public Optional<PlayerEntity> findOne(Long id) {
        return playerRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return playerRepository.existsById(id);
    }

    @Override
    public PlayerEntity partialUpdate(Long id, PlayerEntity playerEntity) {
        playerEntity.setId(id);
        return playerRepository.findById(id).map(existingPlayer -> {
            Optional.ofNullable(playerEntity.getName()).ifPresent(existingPlayer::setName);
            return playerRepository.save(existingPlayer);
        }).orElseThrow(() -> new RuntimeException("Player does not exist"));
    }

    @Override
    public void delete(Long id) {
        playerRepository.deleteById(id);
    }
}
