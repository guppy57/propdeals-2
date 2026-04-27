package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.UserSettings;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserSettingsRepository extends CrudRepository<UserSettings, UUID> {}
