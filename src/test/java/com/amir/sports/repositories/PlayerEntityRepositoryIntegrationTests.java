package com.amir.sports.repositories;

import com.amir.sports.TestDataUtil;
import com.amir.sports.domain.entities.PlayerEntity;
import com.amir.sports.domain.entities.TeamEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// DirtiesContext helps with preventing tests from overlapping (i.e the memory database is refreshed for each test)
// It takes longer at scale, but using it here for convenience
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PlayerEntityRepositoryIntegrationTests {
    
    private final PlayerRepository underTest;
    
    @Autowired
    public PlayerEntityRepositoryIntegrationTests(PlayerRepository underTest) {this.underTest = underTest;}

    @Test
    public void testThatPlayerCanBeCreatedAndRecalled() {
        TeamEntity teamEntity = TestDataUtil.createTestTeamEntityA();
        PlayerEntity playerEntity = TestDataUtil.createTestPlayerEntityA(teamEntity);

        // SAVE returns the version that the database just 'stamped' with an ID
        playerEntity = underTest.save(playerEntity);
        Optional<PlayerEntity> result = underTest.findById(playerEntity.getId());
        assertThat(result).isPresent();
        // Compare the result to savedPlayer, NOT the original player variable
        assertThat(result.get()).isEqualTo(playerEntity);
    }

    @Test
    public void testThatMultiplePlayersCanBeCreatedAndRecalled() {
        TeamEntity teamEntity = TestDataUtil.createTestTeamEntityA();

        PlayerEntity playerEntityA = TestDataUtil.createTestPlayerEntityA(teamEntity);
        playerEntityA = underTest.save(playerEntityA);
        PlayerEntity playerEntityB = TestDataUtil.createTestPlayerEntityB(teamEntity);
        playerEntityB = underTest.save(playerEntityB);
        PlayerEntity playerEntityC = TestDataUtil.createTestPlayerEntityC(teamEntity);
        playerEntityC = underTest.save(playerEntityC);
        Iterable<PlayerEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactlyInAnyOrder(playerEntityA, playerEntityB, playerEntityC);
    }

    @Test
    public void testThatPlayerCanBeUpdated() {
        TeamEntity teamEntity = TestDataUtil.createTestTeamEntityA();

        PlayerEntity playerEntityA = TestDataUtil.createTestPlayerEntityA(teamEntity);
        underTest.save(playerEntityA);

        playerEntityA.setName("UPDATED");
        playerEntityA = underTest.save(playerEntityA);

        Optional<PlayerEntity> result = underTest.findById(playerEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(playerEntityA);

    }

    @Test
    public void testThatPlayerCanBeSearchedByPositionAndTeamName() {
        TeamEntity teamEntityA = TestDataUtil.createTestTeamEntityA();
        TeamEntity teamEntityB = TestDataUtil.createTestTeamEntityB();

        PlayerEntity playerEntityA = TestDataUtil.createTestPlayerEntityA(teamEntityA);
        PlayerEntity playerEntityB = TestDataUtil.createTestPlayerEntityB(teamEntityB);
        underTest.save(playerEntityA);
        underTest.save(playerEntityB);

        Specification<PlayerEntity> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("position")), "%mid%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("teamEntity").get("name")), "%tiger%")
                );
        Page<PlayerEntity> result = underTest.findAll(spec, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getName()).isEqualTo("Yi Sols");
        assertThat(result.getContent().getFirst().getPosition()).isEqualTo("Midfielder");
    }

    @Test
    public void testThatPlayerCanBeDeleted() {
        TeamEntity teamEntity = TestDataUtil.createTestTeamEntityA();

        PlayerEntity playerEntityA = TestDataUtil.createTestPlayerEntityA(teamEntity);
        underTest.save(playerEntityA);

        underTest.deleteById(playerEntityA.getId());

        Optional<PlayerEntity> result = underTest.findById(playerEntityA.getId());
        assertThat(result).isEmpty();
    }
}
