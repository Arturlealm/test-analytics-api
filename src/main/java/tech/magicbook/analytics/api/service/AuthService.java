package tech.magicbook.analytics.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tech.magicbook.analytics.api.dto.LoginRequest;
import tech.magicbook.analytics.api.dto.TokenResponse;
import tech.magicbook.analytics.api.entity.DashboardUser;
import tech.magicbook.analytics.api.exception.InvalidCredentialsException;
import tech.magicbook.analytics.api.repository.DashboardUserRepository;

@Service
public class AuthService {

    private final DashboardUserRepository dashboardUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final long expiration;

    public AuthService(DashboardUserRepository dashboardUserRepository, PasswordEncoder passwordEncoder,
            JwtService jwtService, @Value("${security.jwt.expiration}") long expiration) {
        this.dashboardUserRepository = dashboardUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.expiration = expiration;
    }

    public TokenResponse login(LoginRequest request){
        
        DashboardUser user = dashboardUserRepository.findByEmail(request.email()).orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);

        return new TokenResponse(token, "Bearer", expiration);
    }
}
