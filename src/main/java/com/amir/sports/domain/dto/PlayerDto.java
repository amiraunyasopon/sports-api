package com.amir.sports.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerDto {
    private Long id;

    private String name;

    private String position;

    private TeamDto team;
}
