package com.guppy57.propdeals.controller;

import com.guppy57.propdeals.dto.PropertyNeighborhoodRequest;
import com.guppy57.propdeals.dto.PropertyNeighborhoodResponse;
import com.guppy57.propdeals.security.SupabaseUser;
import com.guppy57.propdeals.service.PropertyNeighborhoodService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/property-neighborhoods")
public class PropertyNeighborhoodController {

    private final PropertyNeighborhoodService service;

    public PropertyNeighborhoodController(PropertyNeighborhoodService service) {
        this.service = service;
    }

    @GetMapping
    public List<PropertyNeighborhoodResponse> listByProperty(@RequestParam UUID propertyId,
                                                             @AuthenticationPrincipal SupabaseUser user) {
        return service.findAllByProperty(propertyId, user.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyNeighborhoodResponse create(@RequestBody PropertyNeighborhoodRequest request,
                                               @AuthenticationPrincipal SupabaseUser user) {
        return service.create(request, user.id());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal SupabaseUser user) {
        service.delete(id, user.id());
    }
}
