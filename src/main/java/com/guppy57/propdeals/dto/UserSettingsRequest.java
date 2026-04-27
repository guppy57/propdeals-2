package com.guppy57.propdeals.dto;

import com.guppy57.propdeals.entity.HouseHackingUnitPreference;

public record UserSettingsRequest(
        Long emergencyFundMonthlyAmount,
        Integer emergencyFundMonths,
        HouseHackingUnitPreference houseHackingUnitPreference
) {}
