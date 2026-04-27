package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("user_settings")
public record UserSettings(
        @Id UUID id,
        Long emergencyFundMonthlyAmount,
        Integer emergencyFundMonths,
        HouseHackingUnitPreference houseHackingUnitPreference
) {}
