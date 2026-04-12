package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.Loan;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanRepository extends CrudRepository<Loan, Integer> {

    @Query("SELECT * FROM loans WHERE user_id = :userId ORDER BY created_at DESC")
    List<Loan> findAllByUserId(@Param("userId") UUID userId);

    @Query("SELECT * FROM loans WHERE id = :id AND user_id = :userId")
    Optional<Loan> findByIdAndUserId(@Param("id") Integer id, @Param("userId") UUID userId);
}
