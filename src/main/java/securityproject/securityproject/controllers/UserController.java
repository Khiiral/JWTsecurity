package securityproject.securityproject.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.UserDTO;
import securityproject.securityproject.exception.UserNotFoundException;
import securityproject.securityproject.models.User;
import securityproject.securityproject.services.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    
    private final UserService userService;

    @GetMapping
    public ResponseEntity<String> userHomePage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String welcomeMessage = "Welcome " + auth.getName() + ", this is the User HomePage";
        //This is a comment
        //this is another comment
        return new ResponseEntity<>(welcomeMessage, HttpStatus.OK);
    }

    

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserData(@PathVariable Long id) throws UserNotFoundException {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDTO userDTO = userService.getUserById(id);
            if(user.getId() == id) {
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
            } else {
                throw new UserNotFoundException("User not found with id: " +id);
            }
        }
}
