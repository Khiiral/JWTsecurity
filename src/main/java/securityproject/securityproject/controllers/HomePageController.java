package securityproject.securityproject.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HomePageController {
    
    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }
}
