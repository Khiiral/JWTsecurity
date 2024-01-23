package securityproject.securityproject.services;

import java.time.LocalDateTime;
import java.util.Optional;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.UserDTO;
import securityproject.securityproject.models.User;
import securityproject.securityproject.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public User save(User newUser) {
        if(newUser.getId() == null) {
            newUser.setCreatedAt(LocalDateTime.now());
        }
        newUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }

    public UserDTO getUserById(Long id) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return mapToDTO(user);
        } else {
            throw new Exception("User not found"); 
        }
    }

    public UserDTO mapToDTO(User user) {
        UserDTO userDTO = UserDTO
                            .builder()
                            .id(user.getId())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .build();
        return userDTO;
    }


    public UserDTO changeUserData(UserDTO userDTO, Long id) throws Exception {
       Optional<User> userOptional = userRepository.findById(id);
       if(userOptional.isPresent()) {
        User user = userOptional.get();

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());

        User updatedUser = userRepository.save(user);

        return mapToDTO(updatedUser);
       } else {
        throw new Exception("User not found");
       }
    }
}
