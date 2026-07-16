package com.amir.sports.repositories;

import com.amir.sports.domain.entities.PlayerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long>
        , PagingAndSortingRepository<PlayerEntity, Long> {
}
