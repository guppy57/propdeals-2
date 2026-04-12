package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.Neighborhood;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NeighborhoodRepository extends CrudRepository<Neighborhood, Long> {

    @Query("SELECT * FROM neighborhoods WHERE user_id = :userId ORDER BY created_at DESC")
    List<Neighborhood> findAllByUserId(@Param("userId") UUID userId);

    @Query("SELECT * FROM neighborhoods WHERE id = :id AND user_id = :userId")
    Optional<Neighborhood> findByIdAndUserId(@Param("id") Long id, @Param("userId") UUID userId);
}
