package com.amir.sports.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name  = "players")
public class PlayerEntity {

    @Id
    private Long id;

    private String name;

    private String position;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private TeamEntity teamEntity;
}
