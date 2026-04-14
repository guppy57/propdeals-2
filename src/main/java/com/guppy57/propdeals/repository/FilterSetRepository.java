package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.FilterSet;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FilterSetRepository extends CrudRepository<FilterSet, Long> {

    @Query("SELECT * FROM filter_sets WHERE user_id = :userId ORDER BY created_at DESC")
    List<FilterSet> findAllByUserId(@Param("userId") UUID userId);

    @Query("SELECT * FROM filter_sets WHERE id = :id AND user_id = :userId")
    Optional<FilterSet> findByIdAndUserId(@Param("id") Long id, @Param("userId") UUID userId);
}
