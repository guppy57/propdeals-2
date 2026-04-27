package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.UserSettingsRequest;
import com.guppy57.propdeals.dto.UserSettingsResponse;
import com.guppy57.propdeals.entity.HouseHackingUnitPreference;
import com.guppy57.propdeals.entity.UserSettings;
import com.guppy57.propdeals.repository.UserSettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserSettingsService {

    private final UserSettingsRepository repository;

    public UserSettingsService(UserSettingsRepository repository) {
        this.repository = repository;
    }

    public UserSettingsResponse findByUserId(UUID userId) {
        return repository.findById(userId)
                .map(UserSettingsResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User settings not found"));
    }

    public UserSettingsResponse update(UUID userId, UserSettingsRequest req) {
        repository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User settings not found"));

        UserSettings updated = new UserSettings(
                userId,
                req.emergencyFundMonthlyAmount() != null ? req.emergencyFundMonthlyAmount() : 1500L,
                req.emergencyFundMonths()         != null ? req.emergencyFundMonths()         : 3,
                req.houseHackingUnitPreference()  != null ? req.houseHackingUnitPreference()  : HouseHackingUnitPreference.LEAST_RENT
        );

        return UserSettingsResponse.from(repository.save(updated));
    }
}
