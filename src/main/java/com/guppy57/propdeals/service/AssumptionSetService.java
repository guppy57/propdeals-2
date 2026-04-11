package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.AssumptionSetRequest;
import com.guppy57.propdeals.dto.AssumptionSetResponse;
import com.guppy57.propdeals.entity.AssumptionSet;
import com.guppy57.propdeals.repository.AssumptionSetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AssumptionSetService {

    private final AssumptionSetRepository repository;

    public AssumptionSetService(AssumptionSetRepository repository) {
        this.repository = repository;
    }

    public List<AssumptionSetResponse> findAll(UUID userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(AssumptionSetResponse::from)
                .toList();
    }

    public AssumptionSetResponse findById(Long id, UUID userId) {
        return repository.findByIdAndUserId(id, userId)
                .map(AssumptionSetResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public AssumptionSetResponse create(AssumptionSetRequest req, UUID userId) {
        if (req.segment() == null || req.description() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "segment and description are required");
        }
        Instant now = Instant.now();
        AssumptionSet entity = toEntity(null, req, userId, now, now);
        return AssumptionSetResponse.from(repository.save(entity));
    }

    public AssumptionSetResponse update(Long id, AssumptionSetRequest req, UUID userId) {
        AssumptionSet existing = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        AssumptionSet updated = toEntity(existing.id(), req, userId, existing.createdAt(), Instant.now());
        return AssumptionSetResponse.from(repository.save(updated));
    }

    public void delete(Long id, UUID userId) {
        if (repository.findByIdAndUserId(id, userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private AssumptionSet toEntity(Long id, AssumptionSetRequest req, UUID userId,
                                   Instant createdAt, Instant updatedAt) {
        return new AssumptionSet(
                id,
                req.segment(),
                req.description(),
                req.isDefault() != null ? req.isDefault() : false,
                userId,
                req.appreciationRate(),
                req.closingCostsRate(),
                req.federalTaxRate(),
                req.stateTaxCode(),
                req.landValuePrcnt(),
                req.rentAppreciationRate(),
                req.propertyTaxRate(),
                req.homeInsuranceRate(),
                req.vacancyRate(),
                req.repairSavingsRate(),
                req.capexReserveRate(),
                req.discountRate(),
                req.sellingCostsRate(),
                req.longtermCapitalGainsTaxRate(),
                req.residentialDepreciationPeriodYrs(),
                req.defaultPropertyConditionScore(),
                req.grossAnnualIncome(),
                req.utilityElectricBase(),
                req.utilityGasBase(),
                req.utilityWaterBase(),
                req.utilityTrashBase(),
                req.utilityInternetBase(),
                req.utilityBaselineSqft(),
                req.mfAppreciationRateOverride(),
                req.rehabContingencyPct(),
                req.holdingCostRateMonthly(),
                req.flipSellingCostsRate(),
                req.shorttermCapitalGainsRate(),
                req.minRoiPct(),
                req.minProfitAmt(),
                createdAt,
                updatedAt
        );
    }
}
