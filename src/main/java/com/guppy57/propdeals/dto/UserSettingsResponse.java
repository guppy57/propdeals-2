package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.HouseHackingUnitPreference;
import com.guppy57.propdeals.entity.UserSettings;

public record UserSettingsResponse(
        Long emergencyFundMonthlyAmount,
        Integer emergencyFundMonths,
        HouseHackingUnitPreference houseHackingUnitPreference
) {
    public static UserSettingsResponse from(UserSettings e) {
        return new UserSettingsResponse(
                e.emergencyFundMonthlyAmount(),
                e.emergencyFundMonths(),
                e.houseHackingUnitPreference()
        );
    }
}
