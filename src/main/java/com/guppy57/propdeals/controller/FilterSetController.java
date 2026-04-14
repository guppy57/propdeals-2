package com.guppy57.propdeals.controller;

import com.guppy57.propdeals.dto.FilterSetRequest;
import com.guppy57.propdeals.dto.FilterSetResponse;
import com.guppy57.propdeals.security.SupabaseUser;
import com.guppy57.propdeals.service.FilterSetService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filter-sets")
public class FilterSetController {

    private final FilterSetService service;

    public FilterSetController(FilterSetService service) {
        this.service = service;
    }

    @GetMapping
    public List<FilterSetResponse> list(@AuthenticationPrincipal SupabaseUser user) {
        return service.findAll(user.id());
    }

    @GetMapping("/{id}")
    public FilterSetResponse get(@PathVariable Long id,
                                 @AuthenticationPrincipal SupabaseUser user) {
        return service.findById(id, user.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilterSetResponse create(@RequestBody FilterSetRequest request,
                                    @AuthenticationPrincipal SupabaseUser user) {
        return service.create(request, user.id());
    }

    @PutMapping("/{id}")
    public FilterSetResponse update(@PathVariable Long id,
                                    @RequestBody FilterSetRequest request,
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
