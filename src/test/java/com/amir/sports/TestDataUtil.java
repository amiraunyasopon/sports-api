package com.amir.sports;

import com.amir.sports.domain.dto.PlayerDto;
import com.amir.sports.domain.dto.TeamDto;
import com.amir.sports.domain.entities.PlayerEntity;
import com.amir.sports.domain.entities.TeamEntity;

public final class TestDataUtil {

    public static TeamEntity createTestTeamEntityA()
    {
        return TeamEntity.builder()
                .name("Thailand Tigers")
                .winLossRatio(0.54)
                .build();
    }

    public static TeamDto createTestTeamDtoA()
    {
        return TeamDto.builder()
                .name("Thailand Tigers")
                .winLossRatio(0.54)
                .build();
    }

    public static TeamEntity createTestTeamEntityB()
    {
        return TeamEntity.builder()
                .name("Bangladesh Bonfires")
                .winLossRatio(0.57)
                .build();
    }

    public static TeamEntity createTestTeamEntityC()
    {
        return TeamEntity.builder()
                .name("Red Ravagers")
                .winLossRatio(0.44)
                .build();
    }

    public static PlayerEntity createTestPlayerEntityA(final TeamEntity teamEntity)
    {
        return PlayerEntity.builder()
                .id(4700L)
                .name("Yi Sols")
                .teamEntity(teamEntity)
                .build();
    }

    public static PlayerDto createTestPlayerDtoA(final TeamDto teamDto)
    {
        return PlayerDto.builder()
                .id(4700L)
                .name("Yi Sols")
                .team(teamDto)
                .build();
    }

    public static PlayerEntity createTestPlayerEntityB(final TeamEntity teamEntity)
    {
        return PlayerEntity.builder()
                .id(3598L)
                .name("Flynn")
                .teamEntity(teamEntity)
                .build();
    }

    public static PlayerEntity createTestPlayerEntityC(final TeamEntity teamEntity)
    {
        return PlayerEntity.builder()
                .id(1998L)
                .name("Chase")
                .teamEntity(teamEntity)
                .build();
    }
}
