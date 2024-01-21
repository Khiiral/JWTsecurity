package securityproject.securityproject.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    
    @GetMapping("/anonymous")
    public String anonymousEndPoint() {
        return "You see this message because the user is not authenticated";
    }

    @GetMapping("/users") 
    @PreAuthorize("hasRole('USER')")
    public String usersEndPoint() {
        return "Only authenticated users with role USER can see this message";
    }

    @GetMapping("/admins") 
    @PreAuthorize("hasRole('ADMIN')")
    public String adminsEndPoint() {
        return "Only ADMINS can see this message";
    }
}
