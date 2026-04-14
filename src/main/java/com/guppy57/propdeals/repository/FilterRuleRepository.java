package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.FilterRule;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilterRuleRepository extends CrudRepository<FilterRule, Long> {

    @Query("SELECT * FROM filter_rules WHERE filter_set_id = :filterSetId ORDER BY id ASC")
    List<FilterRule> findAllByFilterSetId(@Param("filterSetId") Long filterSetId);
}
