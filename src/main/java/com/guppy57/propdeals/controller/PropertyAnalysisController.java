package com.guppy57.propdeals.controller;

import com.guppy57.propdeals.dto.ProcessType;
import com.guppy57.propdeals.dto.PropertyAnalysisRequest;
import com.guppy57.propdeals.dto.PropertyAnalysisResponse;
import com.guppy57.propdeals.dto.QueueMessage;
import com.guppy57.propdeals.security.SupabaseUser;
import com.guppy57.propdeals.service.MessagingService;
import com.guppy57.propdeals.service.PropertyAnalysisService;
import com.guppy57.propdeals.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/analyses")
public class PropertyAnalysisController {
    private final PropertyAnalysisService service;
    private final PropertyService propertyService;
    private final MessagingService messagingService;

    public PropertyAnalysisController(PropertyAnalysisService service, PropertyService propertyService, MessagingService messagingService) {
        this.service = service;
        this.propertyService = propertyService;
        this.messagingService = messagingService;
    }

    @GetMapping
    public List<PropertyAnalysisResponse> getAllByProperty(@PathVariable UUID propertyId, @AuthenticationPrincipal SupabaseUser user) {
        if (propertyService.checkIfPropertyBelongsToUser(propertyId, user.id())) {
            return service.getAnalysesForProperty(propertyId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping
    public PropertyAnalysisResponse create(@RequestBody PropertyAnalysisRequest analysis, @AuthenticationPrincipal SupabaseUser user) {
        PropertyAnalysisResponse pa = service.create(analysis, user.id());
        QueueMessage message = new QueueMessage(pa.id(), ProcessType.NEW_ANALYSIS);
        messagingService.publish(message);
        return pa;
    }

    @PutMapping
    public PropertyAnalysisResponse update(@RequestBody PropertyAnalysisRequest analysis) {
        throw new UnsupportedOperationException("not implemented");
    }
}
