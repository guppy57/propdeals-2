package com.guppy57.propdeals.controller;

import com.guppy57.propdeals.dto.UserSettingsRequest;
import com.guppy57.propdeals.dto.UserSettingsResponse;
import com.guppy57.propdeals.security.SupabaseUser;
import com.guppy57.propdeals.service.UserSettingsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-settings")
public class UserSettingsController {

    private final UserSettingsService service;

    public UserSettingsController(UserSettingsService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public UserSettingsResponse get(@AuthenticationPrincipal SupabaseUser user) {
        return service.findByUserId(user.id());
    }

    @PutMapping("/me")
    public UserSettingsResponse update(
            @RequestBody UserSettingsRequest req,
            @AuthenticationPrincipal SupabaseUser user) {
        return service.update(user.id(), req);
    }
}
