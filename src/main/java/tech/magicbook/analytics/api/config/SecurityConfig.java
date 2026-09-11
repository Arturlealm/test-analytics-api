package tech.magicbook.analytics.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/v1/auth/tokens").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/end-users", "/v1/feature-events", "/v1/ai-usages")
                        .permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/v1/end-users/**").permitAll()
                        .requestMatchers("/docs/**", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()

                        // Todo o restante exige JWT
                        .anyRequest().authenticated())

                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                        
                .oauth2ResourceServer(oauth2 -> oauth2.authenticationEntryPoint(authenticationEntryPoint)
                        .jwt(Customizer.withDefaults()));

        return http.build();
    }
}
