package securityproject.securityproject.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.UserDTO;
import securityproject.securityproject.models.User;
import securityproject.securityproject.services.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final UserService userService;
    
    @GetMapping("/all")
    public String anonymousEndPoint() {
        return "All type of users can see this message";
    }

    @GetMapping("/users") 
    //@PreAuthorize("hasRole('USER')")
    public String usersEndPoint() {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();;
        //boolean isRoleAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        return "Only authenticated users can see this message";
    }

    @GetMapping("/admins") 
    //@PreAuthorize("hasRole('ADMIN')")
    public String adminsEndPoint() {
        return "Only ADMINS can see this message";
    }

    @PutMapping("/admins/updateUser/{id}")
    public ResponseEntity<?> changeRole(@PathVariable Long id,
    @RequestBody UserDTO userDTO) {
        try {
            UserDTO modifiedUser = userService.changeUserData(userDTO, id);
            return new ResponseEntity<UserDTO>(modifiedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("User not found with id" + id, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserData(@PathVariable Long id) throws Exception {
        try {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDTO userDTO = userService.getUserById(id);
            if(user.getId() == userDTO.getId()) {
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("User info details not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("User info details not found", HttpStatus.NOT_FOUND);
        }
        
        
    }
}
