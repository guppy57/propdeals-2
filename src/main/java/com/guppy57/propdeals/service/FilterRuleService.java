package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.FilterRuleRequest;
import com.guppy57.propdeals.dto.FilterRuleResponse;
import com.guppy57.propdeals.entity.FilterOperator;
import com.guppy57.propdeals.entity.FilterRule;
import com.guppy57.propdeals.repository.FilterRuleRepository;
import com.guppy57.propdeals.repository.FilterSetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FilterRuleService {

    private final FilterRuleRepository repository;
    private final FilterSetRepository filterSetRepository;

    public FilterRuleService(FilterRuleRepository repository, FilterSetRepository filterSetRepository) {
        this.repository = repository;
        this.filterSetRepository = filterSetRepository;
    }

    public List<FilterRuleResponse> findAllByFilterSet(Long filterSetId, UUID userId) {
        verifyFilterSetOwnership(filterSetId, userId);
        return repository.findAllByFilterSetId(filterSetId)
                .stream()
                .map(FilterRuleResponse::from)
                .toList();
    }

    public FilterRuleResponse create(FilterRuleRequest req, UUID userId) {
        if (req.filterSetId() == null || req.metric() == null || req.valueA() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "filterSetId, metric, and valueA are required");
        }
        verifyFilterSetOwnership(req.filterSetId(), userId);
        FilterRule entity = toEntity(null, req, Instant.now());
        return FilterRuleResponse.from(repository.save(entity));
    }

    public FilterRuleResponse update(Long id, FilterRuleRequest req, UUID userId) {
        FilterRule existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        verifyFilterSetOwnership(existing.filterSetId(), userId);
        FilterRule updated = toEntity(existing.id(), req, existing.createdAt());
        return FilterRuleResponse.from(repository.save(updated));
    }

    public void delete(Long id, UUID userId) {
        FilterRule existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        verifyFilterSetOwnership(existing.filterSetId(), userId);
        repository.deleteById(id);
    }

    private void verifyFilterSetOwnership(Long filterSetId, UUID userId) {
        if (filterSetRepository.findByIdAndUserId(filterSetId, userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private FilterRule toEntity(Long id, FilterRuleRequest req, Instant createdAt) {
        return new FilterRule(
                id,
                req.filterSetId(),
                req.metric(),
                req.operator() != null ? req.operator() : FilterOperator.BETWEEN,
                req.valueA(),
                req.valueB(),
                createdAt
        );
    }
}
