package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.UnitRequest;
import com.guppy57.propdeals.dto.UnitResponse;
import com.guppy57.propdeals.entity.Unit;
import com.guppy57.propdeals.repository.PropertyRepository;
import com.guppy57.propdeals.repository.UnitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final PropertyRepository propertyRepository;

    public UnitService(UnitRepository unitRepository, PropertyRepository propertyRepository) {
        this.unitRepository = unitRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<UnitResponse> findAllByProperty(UUID propertyId, UUID userId) {
        verifyPropertyOwnership(propertyId, userId);
        return unitRepository.findAllByPropertyId(propertyId)
                .stream()
                .map(UnitResponse::from)
                .toList();
    }

    public UnitResponse findById(Long id, UUID userId) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        verifyPropertyOwnership(unit.propertyId(), userId);
        return UnitResponse.from(unit);
    }

    public UnitResponse create(UnitRequest req, UUID userId) {
        if (req.propertyId() == null || req.unitNum() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "propertyId and unitNum are required");
        }
        verifyPropertyOwnership(req.propertyId(), userId);
        Instant now = Instant.now();
        Unit entity = toEntity(null, req, now, now);
        return UnitResponse.from(unitRepository.save(entity));
    }

    public UnitResponse update(Long id, UnitRequest req, UUID userId) {
        Unit existing = unitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        verifyPropertyOwnership(existing.propertyId(), userId);
        Unit updated = toEntity(existing.id(), req, existing.createdAt(), Instant.now());
        return UnitResponse.from(unitRepository.save(updated));
    }

    public void delete(Long id, UUID userId) {
        Unit existing = unitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        verifyPropertyOwnership(existing.propertyId(), userId);
        unitRepository.deleteById(id);
    }

    private void verifyPropertyOwnership(UUID propertyId, UUID userId) {
        if (propertyRepository.findByIdAndUserId(propertyId, userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private Unit toEntity(Long id, UnitRequest req, Instant createdAt, Instant updatedAt) {
        return new Unit(
                id,
                req.propertyId(),
                req.unitNum(),
                req.beds(),
                req.baths(),
                req.estimatedSqrft(),
                req.rentEstimate(),
                req.rentEstimateLow(),
                req.rentEstimateHigh(),
                createdAt,
                updatedAt
        );
    }
}
