package com.ace4.RestaurantFeedback.controller;


import com.ace4.RestaurantFeedback.model.dto.auth.AuthResponse;
import com.ace4.RestaurantFeedback.model.dto.auth.LoginRequest;
import com.ace4.RestaurantFeedback.model.dto.auth.RegisterRequest;
import com.ace4.RestaurantFeedback.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request){
        try {
            AuthResponse response = userAuthService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse(e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userAuthService.authenticate(request));
    }

}
