package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.ResearchReportRequest;
import com.guppy57.propdeals.dto.ResearchReportResponse;
import com.guppy57.propdeals.entity.ResearchReport;
import com.guppy57.propdeals.repository.PropertyRepository;
import com.guppy57.propdeals.repository.ResearchReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ResearchReportService {

    private final ResearchReportRepository repository;
    private final PropertyRepository propertyRepository;

    public ResearchReportService(ResearchReportRepository repository,
                                 PropertyRepository propertyRepository) {
        this.repository = repository;
        this.propertyRepository = propertyRepository;
    }

    public List<ResearchReportResponse> findAllByProperty(UUID propertyId, UUID userId) {
        verifyPropertyOwnership(propertyId, userId);
        return repository.findAllByPropertyId(propertyId)
                .stream()
                .map(ResearchReportResponse::from)
                .toList();
    }

    public ResearchReportResponse findById(UUID id, UUID userId) {
        ResearchReport report = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        verifyPropertyOwnership(report.propertyId(), userId);
        return ResearchReportResponse.from(report);
    }

    public ResearchReportResponse create(ResearchReportRequest req, UUID userId) {
        if (req.propertyId() == null || req.reportTypeId() == null || req.reportContent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "propertyId, reportTypeId, and reportContent are required");
        }
        verifyPropertyOwnership(req.propertyId(), userId);
        ResearchReport entity = new ResearchReport(
                null,
                req.propertyId(),
                req.reportTypeId(),
                req.reportContent(),
                req.status() != null ? req.status() : "pending",
                req.apiCost(),
                Instant.now()
        );
        return ResearchReportResponse.from(repository.save(entity));
    }

    public void delete(UUID id, UUID userId) {
        ResearchReport existing = repository.findById(id)
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
