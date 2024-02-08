package securityproject.securityproject.services;


import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import securityproject.securityproject.token.TokenRepository;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        
        //Get the header from the request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        
        //If the header is null or doesn't start with Bearer, return
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        //Otherwise, extract the token from the header
        jwt = authHeader.substring(7);
        
        //Find the current token and if it is not null, set to TRUE the expiration and revoke it
        var currentToken = tokenRepository.findByToken(jwt).orElse(null);
        if(currentToken != null) {
            currentToken.setExpired(true);
            currentToken.setRevoked(true);
            tokenRepository.save(currentToken);
        }
    }
}
