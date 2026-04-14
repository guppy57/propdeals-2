package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.RentCompToUnitRequest;
import com.guppy57.propdeals.dto.RentCompToUnitResponse;
import com.guppy57.propdeals.entity.RentCompToUnit;
import com.guppy57.propdeals.entity.Unit;
import com.guppy57.propdeals.repository.PropertyRepository;
import com.guppy57.propdeals.repository.RentCompToUnitRepository;
import com.guppy57.propdeals.repository.UnitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class RentCompToUnitService {

    private final RentCompToUnitRepository repository;
    private final UnitRepository unitRepository;
    private final PropertyRepository propertyRepository;

    public RentCompToUnitService(RentCompToUnitRepository repository,
                                 UnitRepository unitRepository,
                                 PropertyRepository propertyRepository) {
        this.repository = repository;
        this.unitRepository = unitRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<RentCompToUnitResponse> findAllByUnit(Long unitId, UUID userId) {
        verifyUnitOwnership(unitId, userId);
        return repository.findAllByUnitId(unitId)
                .stream()
                .map(RentCompToUnitResponse::from)
                .toList();
    }

    public List<RentCompToUnitResponse> findAllByRentComp(UUID rentCompId) {
        return repository.findAllByRentCompId(rentCompId)
                .stream()
                .map(RentCompToUnitResponse::from)
                .toList();
    }

    public RentCompToUnitResponse create(RentCompToUnitRequest req, UUID userId) {
        if (req.rentCompId() == null || req.unitId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rentCompId and unitId are required");
        }
        verifyUnitOwnership(req.unitId(), userId);
        RentCompToUnit entity = new RentCompToUnit(
                null, req.rentCompId(), req.unitId(),
                req.distanceMiles(), req.correlation(), Instant.now()
        );
        return RentCompToUnitResponse.from(repository.save(entity));
    }

    public void delete(Long id, UUID userId) {
        RentCompToUnit existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        verifyUnitOwnership(existing.unitId(), userId);
        repository.deleteById(id);
    }

    private void verifyUnitOwnership(Long unitId, UUID userId) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (propertyRepository.findByIdAndUserId(unit.propertyId(), userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
