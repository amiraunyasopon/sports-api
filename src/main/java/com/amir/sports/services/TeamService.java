package com.amir.sports.services;

import com.amir.sports.domain.entities.TeamEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TeamService {
    TeamEntity save(TeamEntity teamEntity);

    Page<TeamEntity> findAll(Pageable pageable);

    Optional<TeamEntity> findOne(Long id);

    boolean isExists(Long id);

    TeamEntity partialUpdate(Long id, TeamEntity teamEntity);

    void delete(Long id);
}
