package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.AssumptionSet;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssumptionSetRepository extends CrudRepository<AssumptionSet, Long> {

    @Query("SELECT * FROM assumption_sets WHERE user_id = :userId ORDER BY created_at DESC")
    List<AssumptionSet> findAllByUserId(@Param("userId") UUID userId);

    @Query("SELECT * FROM assumption_sets WHERE id = :id AND user_id = :userId")
    Optional<AssumptionSet> findByIdAndUserId(@Param("id") Long id, @Param("userId") UUID userId);
}
