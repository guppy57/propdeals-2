package com.guppy57.propdeals.controller;

import com.guppy57.propdeals.dto.ReportTypeRequest;
import com.guppy57.propdeals.dto.ReportTypeResponse;
import com.guppy57.propdeals.security.SupabaseUser;
import com.guppy57.propdeals.service.ReportTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/research-types")
public class ReportTypeController {

    private final ReportTypeService service;

    public ReportTypeController(ReportTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReportTypeResponse> list(@AuthenticationPrincipal SupabaseUser user) {
        return service.findAll(user.id());
    }

    @GetMapping("/{id}")
    public ReportTypeResponse get(@PathVariable UUID id,
                                  @AuthenticationPrincipal SupabaseUser user) {
        return service.findById(id, user.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportTypeResponse create(@RequestBody ReportTypeRequest request,
                                     @AuthenticationPrincipal SupabaseUser user) {
        return service.create(request, user.id());
    }

    @PutMapping("/{id}")
    public ReportTypeResponse update(@PathVariable UUID id,
                                     @RequestBody ReportTypeRequest request,
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
