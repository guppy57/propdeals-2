package com.guppy57.propdeals.controller;

import com.guppy57.propdeals.dto.PropertyDetailResponse;
import com.guppy57.propdeals.dto.PropertyRequest;
import com.guppy57.propdeals.dto.PropertyResponse;
import com.guppy57.propdeals.security.SupabaseUser;
import com.guppy57.propdeals.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService service;

    public PropertyController(PropertyService service) {
        this.service = service;
    }

    @GetMapping
    public List<PropertyResponse> list(
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "100")  int size,
            @RequestParam(defaultValue = "")     String q,
            @RequestParam(defaultValue = "")     String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            @AuthenticationPrincipal SupabaseUser user) {
        return service.findAll(user.id(), page, size, q, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public PropertyDetailResponse get(@PathVariable UUID id,
                                      @AuthenticationPrincipal SupabaseUser user) {
        return service.findById(id, user.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyResponse create(@RequestBody PropertyRequest request,
                                   @AuthenticationPrincipal SupabaseUser user) {
        return service.create(request, user.id());
    }

    @PutMapping("/{id}")
    public PropertyResponse update(@PathVariable UUID id,
                                   @RequestBody PropertyRequest request,
                                   @AuthenticationPrincipal SupabaseUser user) {
        return service.update(id, request, user.id());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id,
                       @AuthenticationPrincipal SupabaseUser user) {
        service.delete(id, user.id());
    }
}
