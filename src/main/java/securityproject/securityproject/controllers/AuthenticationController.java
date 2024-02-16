package securityproject.securityproject.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import securityproject.securityproject.validator.SignUpRequestValidator;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    private final SignUpRequestValidator signUpRequestValidator;

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequest request, BindingResult bindingResult) {
        signUpRequestValidator.validate(request, bindingResult);
        
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        JwtAuthenticationResponse response = authenticationService.signup(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody @Valid SignInRequest request) {
        JwtAuthenticationResponse response = authenticationService.signin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
}
