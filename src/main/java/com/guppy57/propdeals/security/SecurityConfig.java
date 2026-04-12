package com.guppy57.propdeals.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${supabase.url}")
    private String supabaseUrl;

    // Publishable (anon) API key — sent as the apikey header when calling /auth/v1/user
    @Value("${supabase.api-key}")
    private String supabaseApiKey;

    @Value("${cors.allowed-origins}")
    private String corsAllowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                    auth.anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwt -> jwt
                            .decoder(jwtDecoder())
                            .jwtAuthenticationConverter(new SupabaseJwtConverter())));
    return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(corsAllowedOrigins.split(",")));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    /**
     * Validates a Supabase access token by calling the Supabase Auth server directly.
     * This approach works for both legacy HS256 tokens and newer RS256 tokens without
     * needing to manage signing keys locally.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        RestClient client = RestClient.create();
        return token -> {
            try {
                Map<String, Object> user = client.get()
                        .uri(supabaseUrl + "/auth/v1/user")
                        .header("apikey", supabaseApiKey)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {});

                if (user == null) throw new BadJwtException("Empty response from Supabase auth");

                @SuppressWarnings("unchecked")
                Map<String, Object> userMetadata =
                        (Map<String, Object>) user.getOrDefault("user_metadata", Map.of());

                return Jwt.withTokenValue(token)
                        .header("alg", "HS256")
                        .subject((String) user.get("id"))
                        .claim("email", user.get("email"))
                        .claim("user_metadata", userMetadata)
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plusSeconds(3600))
                        .build();

            } catch (RestClientResponseException ex) {
                throw new BadJwtException(
                        "Supabase rejected the token: " + ex.getStatusCode()
                        + " — " + ex.getResponseBodyAsString());
            }
        };
    }
}
