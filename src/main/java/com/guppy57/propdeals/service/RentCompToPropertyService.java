package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.RentCompToPropertyRequest;
import com.guppy57.propdeals.dto.RentCompToPropertyResponse;
import com.guppy57.propdeals.entity.RentCompToProperty;
import com.guppy57.propdeals.repository.PropertyRepository;
import com.guppy57.propdeals.repository.RentCompToPropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class RentCompToPropertyService {

    private final RentCompToPropertyRepository repository;
    private final PropertyRepository propertyRepository;

    public RentCompToPropertyService(RentCompToPropertyRepository repository,
                                     PropertyRepository propertyRepository) {
        this.repository = repository;
        this.propertyRepository = propertyRepository;
    }

    public List<RentCompToPropertyResponse> findAllByProperty(UUID propertyId, UUID userId) {
        verifyPropertyOwnership(propertyId, userId);
        return repository.findAllByPropertyId(propertyId)
                .stream()
                .map(RentCompToPropertyResponse::from)
                .toList();
    }

    public List<RentCompToPropertyResponse> findAllByRentComp(UUID rentCompId) {
        return repository.findAllByRentCompId(rentCompId)
                .stream()
                .map(RentCompToPropertyResponse::from)
                .toList();
    }

    public RentCompToPropertyResponse create(RentCompToPropertyRequest req, UUID userId) {
        if (req.rentCompId() == null || req.propertyId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rentCompId and propertyId are required");
        }
        verifyPropertyOwnership(req.propertyId(), userId);
        RentCompToProperty entity = new RentCompToProperty(
                null, req.rentCompId(), req.propertyId(),
                req.distanceMiles(), req.correlation(), Instant.now()
        );
        return RentCompToPropertyResponse.from(repository.save(entity));
    }

    public void delete(Long id, UUID userId) {
        RentCompToProperty existing = repository.findById(id)
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
