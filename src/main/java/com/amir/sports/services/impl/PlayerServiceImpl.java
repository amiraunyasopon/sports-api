package com.amir.sports.services.impl;

import com.amir.sports.domain.entities.PlayerEntity;
import com.amir.sports.repositories.PlayerRepository;
import com.amir.sports.services.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

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
    public Page<PlayerEntity> search(String name, String position, String teamName, Pageable pageable) {
        Specification<PlayerEntity> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (StringUtils.hasText(name)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + name.trim().toLowerCase() + "%")
            );
        }

        if (StringUtils.hasText(position)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("position")),
                            "%" + position.trim().toLowerCase() + "%")
            );
        }

        if (StringUtils.hasText(teamName)) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("teamEntity").get("name")),
                            "%" + teamName.trim().toLowerCase() + "%")
            );
        }

        return playerRepository.findAll(spec, pageable);
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
            Optional.ofNullable(playerEntity.getPosition()).ifPresent(existingPlayer::setPosition);
            Optional.ofNullable(playerEntity.getTeamEntity()).ifPresent(existingPlayer::setTeamEntity);
            return playerRepository.save(existingPlayer);
        }).orElseThrow(() -> new RuntimeException("Player does not exist"));
    }

    @Override
    public void delete(Long id) {
        playerRepository.deleteById(id);
    }
}
