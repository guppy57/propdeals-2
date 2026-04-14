package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.SaleCompRequest;
import com.guppy57.propdeals.dto.SaleCompResponse;
import com.guppy57.propdeals.entity.SaleComp;
import com.guppy57.propdeals.repository.SaleCompRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
public class SaleCompService {

    private final SaleCompRepository repository;

    public SaleCompService(SaleCompRepository repository) {
        this.repository = repository;
    }

    public SaleCompResponse findById(UUID id) {
        return repository.findById(id)
                .map(SaleCompResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public SaleCompResponse create(SaleCompRequest req) {
        if (req.compAddress() == null || req.salePrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "compAddress and salePrice are required");
        }
        SaleComp entity = toEntity(null, req);
        return SaleCompResponse.from(repository.save(entity));
    }

    public SaleCompResponse update(UUID id, SaleCompRequest req) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        SaleComp updated = toEntity(id, req);
        return SaleCompResponse.from(repository.save(updated));
    }

    public void delete(UUID id) {
        if (repository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private SaleComp toEntity(UUID id, SaleCompRequest req) {
        return new SaleComp(
                id,
                req.compAddress(),
                req.beds(),
                req.baths(),
                req.sqft(),
                req.salePrice(),
                req.saleDate(),
                req.pricePerSqft(),
                req.source() != null ? req.source() : "MANUAL",
                req.fetchedAt() != null ? req.fetchedAt() : Instant.now(),
                req.county(),
                req.latitude(),
                req.longitude(),
                req.propertyType()
        );
    }
}
