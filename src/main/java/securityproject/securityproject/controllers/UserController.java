package securityproject.securityproject.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.RequestDTO;
import securityproject.securityproject.dto.UserDTO;
import securityproject.securityproject.exception.UserNotFoundException;
import securityproject.securityproject.models.Token;
import securityproject.securityproject.models.User;
import securityproject.securityproject.services.RequestService;
import securityproject.securityproject.services.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    
    private final UserService userService;

    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<String> userHomePage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String welcomeMessage = "Welcome " + auth.getName() + ", this is the User HomePage";
        return new ResponseEntity<>(welcomeMessage, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String jwt) throws UserNotFoundException {
        UserDTO userDTO = userService.getUserProfile(jwt);
       return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserData(@PathVariable Long id) throws UserNotFoundException {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDTO userDTO = userService.getUserById(id);
            if(userDetails.getUsername().equals(userDTO.getEmail())) {
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
            } else {
                throw new UserNotFoundException("User not found with id: " +id);
            }
    }

    @PostMapping("/sendRequest")
    public ResponseEntity<?> sendRequest(@RequestBody @Valid RequestDTO requestDTO) throws UserNotFoundException {
        RequestDTO newRequest = requestService.addRequest(requestDTO);
        return new ResponseEntity<RequestDTO>(newRequest, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> usersDTO = userService.getAllUsers();
        return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);
    }

    

    
}
