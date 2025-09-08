package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.model.User;
import com.ace4.RestaurantFeedback.model.dto.auth.AuthResponse;
import com.ace4.RestaurantFeedback.model.dto.auth.LoginRequest;
import com.ace4.RestaurantFeedback.model.dto.auth.RegisterRequest;
import com.ace4.RestaurantFeedback.model.enums.Role;
import com.ace4.RestaurantFeedback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmailIgnoreCase(request.email()) != null) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
