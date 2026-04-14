package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.SaleCompToPropertyRequest;
import com.guppy57.propdeals.dto.SaleCompToPropertyResponse;
import com.guppy57.propdeals.entity.SaleCompToProperty;
import com.guppy57.propdeals.repository.PropertyRepository;
import com.guppy57.propdeals.repository.SaleCompToPropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class SaleCompToPropertyService {

    private final SaleCompToPropertyRepository repository;
    private final PropertyRepository propertyRepository;

    public SaleCompToPropertyService(SaleCompToPropertyRepository repository,
                                     PropertyRepository propertyRepository) {
        this.repository = repository;
        this.propertyRepository = propertyRepository;
    }

    public List<SaleCompToPropertyResponse> findAllByProperty(UUID propertyId, UUID userId) {
        verifyPropertyOwnership(propertyId, userId);
        return repository.findAllByPropertyId(propertyId)
                .stream()
                .map(SaleCompToPropertyResponse::from)
                .toList();
    }

    public List<SaleCompToPropertyResponse> findAllBySaleComp(UUID saleCompId) {
        return repository.findAllBySaleCompId(saleCompId)
                .stream()
                .map(SaleCompToPropertyResponse::from)
                .toList();
    }

    public SaleCompToPropertyResponse create(SaleCompToPropertyRequest req, UUID userId) {
        if (req.saleCompId() == null || req.propertyId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "saleCompId and propertyId are required");
        }
        verifyPropertyOwnership(req.propertyId(), userId);
        SaleCompToProperty entity = new SaleCompToProperty(
                null, req.saleCompId(), req.propertyId(),
                req.distanceMiles(), req.correlation(), Instant.now()
        );
        return SaleCompToPropertyResponse.from(repository.save(entity));
    }

    public void delete(Long id, UUID userId) {
        SaleCompToProperty existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        verifyPropertyOwnership(existing.propertyId(), userId);
        repository.deleteById(id);
    }

    private void verifyPropertyOwnership(UUID propertyId, UUID userId) {
        if (propertyRepository.findByIdAndUserId(propertyId, userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
