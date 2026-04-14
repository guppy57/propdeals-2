package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.FilterSetRequest;
import com.guppy57.propdeals.dto.FilterSetResponse;
import com.guppy57.propdeals.entity.FilterSet;
import com.guppy57.propdeals.repository.FilterSetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FilterSetService {

    private final FilterSetRepository repository;

    public FilterSetService(FilterSetRepository repository) {
        this.repository = repository;
    }

    public List<FilterSetResponse> findAll(UUID userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(FilterSetResponse::from)
                .toList();
    }

    public FilterSetResponse findById(Long id, UUID userId) {
        return repository.findByIdAndUserId(id, userId)
                .map(FilterSetResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public FilterSetResponse create(FilterSetRequest req, UUID userId) {
        if (req.name() == null || req.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        FilterSet entity = toEntity(null, req, userId, Instant.now());
        return FilterSetResponse.from(repository.save(entity));
    }

    public FilterSetResponse update(Long id, FilterSetRequest req, UUID userId) {
        FilterSet existing = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        FilterSet updated = toEntity(existing.id(), req, userId, existing.createdAt());
        return FilterSetResponse.from(repository.save(updated));
    }

    public void delete(Long id, UUID userId) {
        if (repository.findByIdAndUserId(id, userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private FilterSet toEntity(Long id, FilterSetRequest req, UUID userId, Instant createdAt) {
        return new FilterSet(
                id,
                req.name(),
                req.description(),
                req.dealType(),
                req.isDefault() != null ? req.isDefault() : false,
                userId,
                createdAt
        );
    }
}
