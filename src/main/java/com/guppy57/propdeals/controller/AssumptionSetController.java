package com.guppy57.propdeals.controller;

import com.guppy57.propdeals.dto.AssumptionSetRequest;
import com.guppy57.propdeals.dto.AssumptionSetResponse;
import com.guppy57.propdeals.security.SupabaseUser;
import com.guppy57.propdeals.service.AssumptionSetService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assumption-sets")
public class AssumptionSetController {

    private final AssumptionSetService service;

    public AssumptionSetController(AssumptionSetService service) {
        this.service = service;
    }

    @GetMapping
    public List<AssumptionSetResponse> list(@AuthenticationPrincipal SupabaseUser user) {
        return service.findAll(user.id());
    }

    @GetMapping("/{id}")
    public AssumptionSetResponse get(@PathVariable Long id,
                                     @AuthenticationPrincipal SupabaseUser user) {
        return service.findById(id, user.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssumptionSetResponse create(@RequestBody AssumptionSetRequest request,
                                        @AuthenticationPrincipal SupabaseUser user) {
        return service.create(request, user.id());
    }

    @PutMapping("/{id}")
    public AssumptionSetResponse update(@PathVariable Long id,
                                        @RequestBody AssumptionSetRequest request,
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
