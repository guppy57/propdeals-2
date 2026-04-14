package com.guppy57.propdeals.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps to the public.profiles table.
 * id is the Supabase auth.users UUID — populated automatically by the
 * on_auth_user_created trigger. No userId field: id IS the user.
 */
@Table("profiles")
public record Profile(

        @Id UUID id,

        String email,
        String fullName,
        String avatarUrl,

        Instant createdAt
) {}
