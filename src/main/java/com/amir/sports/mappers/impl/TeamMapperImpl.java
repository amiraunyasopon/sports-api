package com.amir.sports.mappers.impl;

import com.amir.sports.domain.dto.TeamDto;
import com.amir.sports.domain.entities.TeamEntity;
import com.amir.sports.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TeamMapperImpl implements Mapper<TeamEntity, TeamDto> {

    private final ModelMapper modelMapper;

    public TeamMapperImpl(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    @Override
    public TeamDto mapTo(TeamEntity teamEntity) {
        return modelMapper.map(teamEntity, TeamDto.class);
    }

    @Override
    public TeamEntity mapFrom(TeamDto teamDto) {
        return modelMapper.map(teamDto, TeamEntity.class);
    }
}
