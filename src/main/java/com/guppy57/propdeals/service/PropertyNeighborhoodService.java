package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.PropertyNeighborhoodRequest;
import com.guppy57.propdeals.dto.PropertyNeighborhoodResponse;
import com.guppy57.propdeals.entity.PropertyNeighborhood;
import com.guppy57.propdeals.repository.PropertyNeighborhoodRepository;
import com.guppy57.propdeals.repository.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class PropertyNeighborhoodService {

    private final PropertyNeighborhoodRepository repository;
    private final PropertyRepository propertyRepository;

    public PropertyNeighborhoodService(PropertyNeighborhoodRepository repository,
                                       PropertyRepository propertyRepository) {
        this.repository = repository;
        this.propertyRepository = propertyRepository;
    }

    public List<PropertyNeighborhoodResponse> findAllByProperty(UUID propertyId, UUID userId) {
        verifyPropertyOwnership(propertyId, userId);
        return repository.findAllByPropertyId(propertyId)
                .stream()
                .map(PropertyNeighborhoodResponse::from)
                .toList();
    }

    public PropertyNeighborhoodResponse create(PropertyNeighborhoodRequest req, UUID userId) {
        if (req.propertyId() == null || req.neighborhoodId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "propertyId and neighborhoodId are required");
        }
        verifyPropertyOwnership(req.propertyId(), userId);
        Instant now = Instant.now();
        PropertyNeighborhood entity = new PropertyNeighborhood(null, req.propertyId(), req.neighborhoodId(), now, now);
        return PropertyNeighborhoodResponse.from(repository.save(entity));
    }

    public void delete(Long id, UUID userId) {
        PropertyNeighborhood existing = repository.findById(id)
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
