package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.ReportTypeRequest;
import com.guppy57.propdeals.dto.ReportTypeResponse;
import com.guppy57.propdeals.entity.ReportType;
import com.guppy57.propdeals.repository.ReportTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ReportTypeService {

    private final ReportTypeRepository repository;

    public ReportTypeService(ReportTypeRepository repository) {
        this.repository = repository;
    }

    public List<ReportTypeResponse> findAll(UUID userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(ReportTypeResponse::from)
                .toList();
    }

    public ReportTypeResponse findById(UUID id, UUID userId) {
        return repository.findByIdAndUserId(id, userId)
                .map(ReportTypeResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ReportTypeResponse create(ReportTypeRequest req, UUID userId) {
        if (req.researchType() == null || req.researchType().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "researchType is required");
        }
        if (req.prompt() == null || req.prompt().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "prompt is required");
        }
        Instant now = Instant.now();
        ReportType entity = toEntity(null, req, userId, now, now);
        return ReportTypeResponse.from(repository.save(entity));
    }

    public ReportTypeResponse update(UUID id, ReportTypeRequest req, UUID userId) {
        ReportType existing = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ReportType updated = toEntity(existing.id(), req, userId, existing.createdAt(), Instant.now());
        return ReportTypeResponse.from(repository.save(updated));
    }

    public void delete(UUID id, UUID userId) {
        if (repository.findByIdAndUserId(id, userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private ReportType toEntity(UUID id, ReportTypeRequest req, UUID userId,
                                Instant createdAt, Instant updatedAt) {
        return new ReportType(
                id,
                req.researchType(),
                req.prompt(),
                userId,
                createdAt,
                updatedAt
        );
    }
}
