package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.PropertyAnalysisRequest;
import com.guppy57.propdeals.dto.PropertyAnalysisResponse;
import com.guppy57.propdeals.entity.AnalysisStatus;
import com.guppy57.propdeals.entity.DealType;
import com.guppy57.propdeals.entity.PropertyAnalysis;
import com.guppy57.propdeals.repository.PropertyAnalysisRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class PropertyAnalysisService {

    private final PropertyAnalysisRepository repository;

    public PropertyAnalysisService(PropertyAnalysisRepository repository) {
        this.repository = repository;
    }

    public List<PropertyAnalysisResponse> getAnalysesForProperty(UUID propertyId) {
        return repository.findAllByPropertyId(propertyId)
                .stream()
                .map(PropertyAnalysisResponse::from)
                .toList();
    }

    public PropertyAnalysisResponse create(PropertyAnalysisRequest req, UUID userId) {
        Instant now = Instant.now();
        PropertyAnalysis entity = toEntity(null, req, userId, now, now);
        return PropertyAnalysisResponse.from(repository.save(entity));
    }

    public void triggerReanalysis() {}

    private PropertyAnalysis toEntity(UUID id, PropertyAnalysisRequest req, UUID userId, Instant createdAt, Instant updatedAt) {
        return new PropertyAnalysis(
                id,
                req.propertyId(),
                req.dealType() != null ? req.dealType() : DealType.LONG_TERM_RENTAL,
                req.isPrimary(),
                req.status() != null ? req.status() : AnalysisStatus.ACTIVE,
                req.loanId(),
                req.assumptionSetId(),
                req.purchasePriceOverride(),
                req.sellerCredits(),
                req.sellerPaysBuyersAgentFee(),
                req.notes(),
                createdAt,
                updatedAt
        );
    }
}