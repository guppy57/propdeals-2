package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.LoanRequest;
import com.guppy57.propdeals.dto.LoanResponse;
import com.guppy57.propdeals.entity.Loan;
import com.guppy57.propdeals.repository.LoanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class LoanService {

    private final LoanRepository repository;

    public LoanService(LoanRepository repository) {
        this.repository = repository;
    }

    public List<LoanResponse> findAll(UUID userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(LoanResponse::from)
                .toList();
    }

    public LoanResponse findById(Integer id, UUID userId) {
        return repository.findByIdAndUserId(id, userId)
                .map(LoanResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public LoanResponse create(LoanRequest req, UUID userId) {
        if (req.name() == null || req.loanType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name and loanType are required");
        }
        Loan entity = toEntity(null, req, userId, Instant.now());
        return LoanResponse.from(repository.save(entity));
    }

    public LoanResponse update(Integer id, LoanRequest req, UUID userId) {
        Loan existing = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Loan updated = toEntity(existing.id(), req, userId, existing.createdAt());
        return LoanResponse.from(repository.save(updated));
    }

    public void delete(Integer id, UUID userId) {
        if (repository.findByIdAndUserId(id, userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private Loan toEntity(Integer id, LoanRequest req, UUID userId, Instant createdAt) {
        return new Loan(
                id,
                req.name(),
                req.loanType(),
                req.isDefault() != null ? req.isDefault() : false,
                userId,
                req.interestRate(),
                req.aprRate(),
                req.downPaymentRate(),
                req.years(),
                req.mipUpfrontRate(),
                req.mipAnnualRate(),
                req.upfrontDiscounts() != null ? req.upfrontDiscounts() : 0.0,
                req.lenderFees(),
                req.pmiAmountOverride(),
                req.points(),
                createdAt
        );
    }
}
