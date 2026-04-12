package com.guppy57.propdeals.controller;

import com.guppy57.propdeals.dto.LoanRequest;
import com.guppy57.propdeals.dto.LoanResponse;
import com.guppy57.propdeals.security.SupabaseUser;
import com.guppy57.propdeals.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @GetMapping
    public List<LoanResponse> list(@AuthenticationPrincipal SupabaseUser user) {
        return service.findAll(user.id());
    }

    @GetMapping("/{id}")
    public LoanResponse get(@PathVariable Integer id,
                            @AuthenticationPrincipal SupabaseUser user) {
        return service.findById(id, user.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse create(@RequestBody LoanRequest request,
                               @AuthenticationPrincipal SupabaseUser user) {
        return service.create(request, user.id());
    }

    @PutMapping("/{id}")
    public LoanResponse update(@PathVariable Integer id,
                               @RequestBody LoanRequest request,
                               @AuthenticationPrincipal SupabaseUser user) {
        return service.update(id, request, user.id());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id,
                       @AuthenticationPrincipal SupabaseUser user) {
        service.delete(id, user.id());
    }
}
