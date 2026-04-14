package com.guppy57.propdeals.controller;

import com.guppy57.propdeals.dto.UnitRequest;
import com.guppy57.propdeals.dto.UnitResponse;
import com.guppy57.propdeals.security.SupabaseUser;
import com.guppy57.propdeals.service.UnitService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/units")
public class UnitController {

    private final UnitService service;

    public UnitController(UnitService service) {
        this.service = service;
    }

    @GetMapping
    public List<UnitResponse> listByProperty(@RequestParam UUID propertyId,
                                             @AuthenticationPrincipal SupabaseUser user) {
        return service.findAllByProperty(propertyId, user.id());
    }

    @GetMapping("/{id}")
    public UnitResponse get(@PathVariable Long id,
                            @AuthenticationPrincipal SupabaseUser user) {
        return service.findById(id, user.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnitResponse create(@RequestBody UnitRequest request,
                               @AuthenticationPrincipal SupabaseUser user) {
        return service.create(request, user.id());
    }

    @PutMapping("/{id}")
    public UnitResponse update(@PathVariable Long id,
                               @RequestBody UnitRequest request,
                               @AuthenticationPrincipal SupabaseUser user) {
        return service.update(id, request, user.id());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal SupabaseUser user) {
        service.delete(id, user.id());
    }
}
