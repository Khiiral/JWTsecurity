package securityproject.securityproject.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.JwtAuthenticationResponse;
import securityproject.securityproject.dto.SignInRequest;
import securityproject.securityproject.dto.SignUpRequest;
import securityproject.securityproject.services.AuthenticationService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody @Valid SignUpRequest request) {
        JwtAuthenticationResponse response = authenticationService.signup(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody @Valid SignInRequest request) {
        JwtAuthenticationResponse response = authenticationService.signin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
}
