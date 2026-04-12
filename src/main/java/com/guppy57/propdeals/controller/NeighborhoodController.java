package com.guppy57.propdeals.controller;

import com.guppy57.propdeals.dto.NeighborhoodRequest;
import com.guppy57.propdeals.dto.NeighborhoodResponse;
import com.guppy57.propdeals.security.SupabaseUser;
import com.guppy57.propdeals.service.NeighborhoodService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/neighborhoods")
public class NeighborhoodController {

    private final NeighborhoodService service;

    public NeighborhoodController(NeighborhoodService service) {
        this.service = service;
    }

    @GetMapping
    public List<NeighborhoodResponse> list(@AuthenticationPrincipal SupabaseUser user) {
        return service.findAll(user.id());
    }

    @GetMapping("/{id}")
    public NeighborhoodResponse get(@PathVariable Long id,
                                    @AuthenticationPrincipal SupabaseUser user) {
        return service.findById(id, user.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NeighborhoodResponse create(@RequestBody NeighborhoodRequest request,
                                       @AuthenticationPrincipal SupabaseUser user) {
        return service.create(request, user.id());
    }

    @PutMapping("/{id}")
    public NeighborhoodResponse update(@PathVariable Long id,
                                       @RequestBody NeighborhoodRequest request,
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
