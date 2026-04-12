package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.NeighborhoodRequest;
import com.guppy57.propdeals.dto.NeighborhoodResponse;
import com.guppy57.propdeals.entity.Neighborhood;
import com.guppy57.propdeals.repository.NeighborhoodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class NeighborhoodService {

    private final NeighborhoodRepository repository;

    public NeighborhoodService(NeighborhoodRepository repository) {
        this.repository = repository;
    }

    public List<NeighborhoodResponse> findAll(UUID userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(NeighborhoodResponse::from)
                .toList();
    }

    public NeighborhoodResponse findById(Long id, UUID userId) {
        return repository.findByIdAndUserId(id, userId)
                .map(NeighborhoodResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public NeighborhoodResponse create(NeighborhoodRequest req, UUID userId) {
        if (req.name() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        Instant now = Instant.now();
        Neighborhood entity = toEntity(null, req, userId, now, now);
        return NeighborhoodResponse.from(repository.save(entity));
    }

    public NeighborhoodResponse update(Long id, NeighborhoodRequest req, UUID userId) {
        Neighborhood existing = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Neighborhood updated = toEntity(existing.id(), req, userId, existing.createdAt(), Instant.now());
        return NeighborhoodResponse.from(repository.save(updated));
    }

    public void delete(Long id, UUID userId) {
        if (repository.findByIdAndUserId(id, userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private Neighborhood toEntity(Long id, NeighborhoodRequest req, UUID userId,
                                  Instant createdAt, Instant updatedAt) {
        return new Neighborhood(
                id,
                req.name(),
                req.nicheComMappedName(),
                req.letterGrade(),
                req.nicheComLetterGrade(),
                req.city(),
                req.state(),
                req.country(),
                userId,
                createdAt,
                updatedAt
        );
    }
}
