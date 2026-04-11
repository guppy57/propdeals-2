package com.guppy57.propdeals.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Converts a validated Supabase JWT into a {@link UsernamePasswordAuthenticationToken}
 * whose principal is a {@link SupabaseUser}.
 *
 * Supabase JWT claims used:
 * <ul>
 *   <li>{@code sub}            → user UUID</li>
 *   <li>{@code email}          → email address</li>
 *   <li>{@code user_metadata.name}       → display name (from GitHub)</li>
 *   <li>{@code user_metadata.full_name}  → full name</li>
 *   <li>{@code user_metadata.avatar_url} → avatar URL</li>
 * </ul>
 */
public class SupabaseJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        UUID id = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");

        Map<String, Object> userMeta = jwt.getClaimAsMap("user_metadata");
        String name      = stringClaim(userMeta, "name");
        String fullName  = stringClaim(userMeta, "full_name");
        String avatarUrl = stringClaim(userMeta, "avatar_url");

        SupabaseUser principal = new SupabaseUser(id, email, name, fullName, avatarUrl);
        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    private String stringClaim(Map<String, Object> map, String key) {
        if (map == null) return null;
        Object value = map.get(key);
        return value instanceof String s ? s : null;
    }
}
