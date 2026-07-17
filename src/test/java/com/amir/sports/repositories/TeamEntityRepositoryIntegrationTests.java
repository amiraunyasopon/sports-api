package com.amir.sports.repositories;

import com.amir.sports.TestDataUtil;
import com.amir.sports.domain.entities.TeamEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// DirtiesContext helps with preventing tests from overlapping (i.e the memory database is refreshed for each test)
// It takes longer at scale, but using it here for convenience
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TeamEntityRepositoryIntegrationTests {

    private final TeamRepository underTest;

    // constructor injection requires Autowired
    // tells Spring to inject dependencies as declared
    @Autowired
    public TeamEntityRepositoryIntegrationTests(TeamRepository underTest) {this.underTest = underTest;}

    @Test
    public void testThatTeamCanBeCreatedAndRecalled()
    {
        TeamEntity teamEntity = TestDataUtil.createTestTeamEntityA();
        underTest.save(teamEntity);
        Optional<TeamEntity> result = underTest.findById(teamEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(teamEntity);
    }

    @Test
    public void testThatMultipleTeamsCanBeCreatedAndRecalled()
    {
        TeamEntity teamEntityA = TestDataUtil.createTestTeamEntityA();
        underTest.save(teamEntityA);
        TeamEntity teamEntityB = TestDataUtil.createTestTeamEntityB();
        underTest.save(teamEntityB);
        TeamEntity teamEntityC = TestDataUtil.createTestTeamEntityC();
        underTest.save(teamEntityC);
        Iterable<TeamEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(teamEntityA, teamEntityB, teamEntityC);
    }

    @Test
    public void testThatTeamCanBeUpdated() {
        TeamEntity teamEntityA = TestDataUtil.createTestTeamEntityA();
        underTest.save(teamEntityA);
        teamEntityA.setName("UPDATED");
        underTest.save(teamEntityA);
        Optional<TeamEntity> result = underTest.findById(teamEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(teamEntityA);
    }

    @Test
    public void testThatTeamCanBeDeleted() {
        TeamEntity teamEntityA = TestDataUtil.createTestTeamEntityA();
        underTest.save(teamEntityA);
        underTest.deleteById(teamEntityA.getId());
        Optional<TeamEntity> result = underTest.findById(teamEntityA.getId());
        assertThat(result).isEmpty();
    }
}
