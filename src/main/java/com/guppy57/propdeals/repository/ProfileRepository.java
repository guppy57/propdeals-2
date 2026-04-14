package com.guppy57.propdeals.repository;

import com.guppy57.propdeals.entity.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProfileRepository extends CrudRepository<Profile, UUID> {
    // findById(UUID) from CrudRepository is sufficient — id IS the user UUID
}
