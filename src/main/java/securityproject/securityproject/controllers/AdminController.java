package securityproject.securityproject.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.UserDTO;
import securityproject.securityproject.exception.RequestNotFoundException;
import securityproject.securityproject.exception.UserNotFoundException;
import securityproject.securityproject.services.RequestService;
import securityproject.securityproject.services.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    
    private final UserService userService;
    private final RequestService requestService;


    @GetMapping
    public ResponseEntity<String> adminHomePage() {
        return new ResponseEntity<>("ADMIN HomePage", HttpStatus.OK);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUserData(@PathVariable Long id, @RequestBody UserDTO userDTO) throws UserNotFoundException {
            UserDTO modifiedUser = userService.changeUserData(userDTO, id);
            return new ResponseEntity<>(modifiedUser, HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> usersDTO = userService.getAllUsers();
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);

    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) throws UserNotFoundException {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.OK);
    }
    
     @PutMapping("/request/{id}/accept")
    public ResponseEntity<String> updateUserRequest(@PathVariable Long id) throws RequestNotFoundException {
        requestService.acceptRequest(id);
        return new ResponseEntity<>("Request accepted", HttpStatus.OK);
    }
        
    

}
