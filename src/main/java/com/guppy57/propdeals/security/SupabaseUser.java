package com.guppy57.propdeals.security;

import java.util.UUID;

/**
 * Authenticated principal populated from a Supabase JWT.
 * Available in controllers via {@code @AuthenticationPrincipal SupabaseUser user}.
 */
public record SupabaseUser(
        UUID id,
        String email,
        String name,
        String fullName,
        String avatarUrl
) {}
