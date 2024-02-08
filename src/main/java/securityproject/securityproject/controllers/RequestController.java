package securityproject.securityproject.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.RequestDTO;
import securityproject.securityproject.exception.UserNotFoundException;
import securityproject.securityproject.models.User;
import securityproject.securityproject.services.RequestService;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
public class RequestController {
    
    private final RequestService requestService;

    @GetMapping("/my-requests")
    public ResponseEntity<List<RequestDTO>> getRequestsByCurrentUser() throws UserNotFoundException {
        User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<RequestDTO> response = requestService.getAllRequestsByCurrentUser(currentUser);
            System.out.println("Requests of: " + currentUser.getFirstName());
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
   
}
