package com.amir.sports.services.impl;

import com.amir.sports.domain.entities.TeamEntity;
import com.amir.sports.services.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.amir.sports.repositories.TeamRepository;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {this.teamRepository = teamRepository;}

    @Override
    public TeamEntity save(TeamEntity teamEntity) {
        return teamRepository.save(teamEntity);
    }

    @Override
    public Page<TeamEntity> findAll(Pageable pageable) {
        return teamRepository.findAll(pageable);
    }

    @Override
    public Optional<TeamEntity> findOne(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return teamRepository.existsById(id);
    }

    @Override
    public TeamEntity partialUpdate(Long id, TeamEntity teamEntity) {
        teamEntity.setId(id);
        return teamRepository.findById(id).map(existingTeam -> {
            Optional.ofNullable(teamEntity.getName()).ifPresent(existingTeam::setName);
            Optional.ofNullable(teamEntity.getWinLossRatio()).ifPresent(existingTeam::setWinLossRatio);
            return teamRepository.save(existingTeam);
        }).orElseThrow(() -> new RuntimeException("Team does not exist"));
    }

    @Override
    public void delete(Long id) {
        teamRepository.deleteById(id);
    }
}
