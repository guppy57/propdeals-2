package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.RentCompRequest;
import com.guppy57.propdeals.dto.RentCompResponse;
import com.guppy57.propdeals.entity.RentComp;
import com.guppy57.propdeals.repository.RentCompRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
public class RentCompService {

    private final RentCompRepository repository;

    public RentCompService(RentCompRepository repository) {
        this.repository = repository;
    }

    public RentCompResponse findById(UUID id) {
        return repository.findById(id)
                .map(RentCompResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public RentCompResponse create(RentCompRequest req) {
        if (req.compAddress() == null || req.rentAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "compAddress and rentAmount are required");
        }
        RentComp entity = toEntity(null, req);
        return RentCompResponse.from(repository.save(entity));
    }

    public RentCompResponse update(UUID id, RentCompRequest req) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        RentComp updated = toEntity(id, req);
        return RentCompResponse.from(repository.save(updated));
    }

    public void delete(UUID id) {
        if (repository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private RentComp toEntity(UUID id, RentCompRequest req) {
        return new RentComp(
                id,
                req.compAddress(),
                req.beds(),
                req.baths(),
                req.sqft(),
                req.rentAmount(),
                req.source() != null ? req.source() : "RENTCAST",
                req.fetchedAt() != null ? req.fetchedAt() : Instant.now(),
                req.county(),
                req.latitude(),
                req.longitude(),
                req.propertyType(),
                req.lotSize(),
                req.builtIn(),
                req.status(),
                req.daysOld()
        );
    }
}
