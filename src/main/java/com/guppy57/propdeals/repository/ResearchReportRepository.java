package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.ResearchReport;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ResearchReportRepository extends CrudRepository<ResearchReport, UUID> {

    @Query("SELECT * FROM research_reports WHERE property_id = :propertyId ORDER BY created_at DESC")
    List<ResearchReport> findAllByPropertyId(@Param("propertyId") UUID propertyId);

    @Query("SELECT * FROM research_reports WHERE report_type_id = :reportTypeId ORDER BY created_at DESC")
    List<ResearchReport> findAllByReportTypeId(@Param("reportTypeId") UUID reportTypeId);
}
