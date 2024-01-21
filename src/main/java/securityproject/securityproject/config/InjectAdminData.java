package securityproject.securityproject.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import securityproject.securityproject.models.Role;
import securityproject.securityproject.models.User;
import securityproject.securityproject.repositories.UserRepository;
import securityproject.securityproject.services.UserService;

@Component
@RequiredArgsConstructor
@Slf4j
public class InjectAdminData implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    @Override
    public void run(String... args) throws Exception {
        
       if(userRepository.count() == 0) {
        User admin = User
                        .builder()
                        .firstName("admin")
                        .lastName("admin")
                        .email("admin@admin.com")
                        .password(passwordEncoder.encode("password"))
                        .role(Role.ROLE_ADMIN)
                        .build();
            
        userService.save(admin);
        log.debug("Created ADMIN - {}", admin);
       }
    }
}
