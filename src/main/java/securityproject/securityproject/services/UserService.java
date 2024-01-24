package securityproject.securityproject.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.UserDTO;
import securityproject.securityproject.exception.UserNotFoundException;
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

    public UserDTO getUserById(Long id) throws UserNotFoundException{
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return mapToDTO(user);
        } else {
            throw new UserNotFoundException("User not found with id: " +id); 
        }
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> usersDTO = users
                .stream()
                .map(user -> mapToDTO(user))
                .collect(Collectors.toList());
        return usersDTO;   
    }


    public UserDTO changeUserData(UserDTO userDTO, Long id) throws UserNotFoundException {
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
            throw new UserNotFoundException("User not found with id: " +id);
       }
    }

    public void deleteUserById(Long id) throws UserNotFoundException {
       User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public boolean hasRole(String roleName) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getAuthorities().stream()
                .anyMatch(role -> 
                    role.getAuthority().equals(roleName));
    }

    public UserDTO mapToDTO(User user) {
        UserDTO userDTO = UserDTO
                            .builder()
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .build();
        return userDTO;
    }
}
