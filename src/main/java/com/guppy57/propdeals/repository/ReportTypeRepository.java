package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.ReportType;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportTypeRepository extends CrudRepository<ReportType, UUID> {

    @Query("SELECT * FROM report_types WHERE user_id = :userId ORDER BY created_at DESC")
    List<ReportType> findAllByUserId(@Param("userId") UUID userId);

    @Query("SELECT * FROM report_types WHERE id = :id AND user_id = :userId")
    Optional<ReportType> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}
