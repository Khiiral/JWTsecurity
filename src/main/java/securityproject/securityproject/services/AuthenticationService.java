package securityproject.securityproject.services;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.JwtAuthenticationResponse;
import securityproject.securityproject.dto.SignInRequest;
import securityproject.securityproject.dto.SignUpRequest;
import securityproject.securityproject.models.Role;
import securityproject.securityproject.models.User;
import securityproject.securityproject.repositories.UserRepository;
import securityproject.securityproject.token.Token;
import securityproject.securityproject.token.TokenRepository;
import securityproject.securityproject.token.TokenType;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    

    public JwtAuthenticationResponse signup(SignUpRequest request) {
        User user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        
        user = userService.save(user);
        String jwt = jwtService.generateToken(user.getUsername());

        saveUserToken(user, jwt);

        return JwtAuthenticationResponse.builder().token(jwt).build();

    }

    private void saveUserToken(User user, String jwt) {
        Token token = Token
                    .builder()
                    .user(user)
                    .token(jwt)
                    .tokenType(TokenType.BEARER)
                    .expired(false)
                    .revoked(false)
                    .build();

        tokenRepository.save(token);
    }

    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        String jwt = jwtService.generateToken(user.getUsername());
        revokeAllUserTokens(user);
        saveUserToken(user, jwt);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    private void revokeAllUserTokens(User user) {
        List<Token> allUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(allUserTokens.isEmpty()) {
            return;
        }
        allUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(allUserTokens);
    }
}

