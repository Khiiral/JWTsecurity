package securityproject.securityproject.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import securityproject.securityproject.dto.UserDTO;
import securityproject.securityproject.exception.UserNotFoundException;
import securityproject.securityproject.models.Role;
import securityproject.securityproject.models.User;
import securityproject.securityproject.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)     
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    public void userService_createUser_returnUserDTO() {

        User user1 = User.builder()
                                .firstName("user1FirstName")
                                .lastName("user1LastName")
                                .email("user1@gmail.com")
                                .password("passwordUser1")
                                .build();

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);

        User savedUser = userService.save(user1);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getEmail()).isEqualTo("user1@gmail.com");
    }

    @Test
    public void userService_getAllUsers_returnUsersDTO() {
        User retrievedUser1 = Mockito.mock(User.class);
        User retrievedUser2 = Mockito.mock(User.class);

        when(userRepository.findAll()).thenReturn(List.of(retrievedUser1, retrievedUser2));

        List<UserDTO> usersDTO = userService.getAllUsers();

        Assertions.assertThat(usersDTO).isNotNull();
        Assertions.assertThat((usersDTO.size())).isEqualTo(2);
    }


    @Test
    public void userService_getUserById_returnUserDTO() throws UserNotFoundException {

        User user1 = User.builder()
                                .firstName("user1FirstName")
                                .lastName("user1LastName")
                                .email("user1@gmail.com")
                                .password("passwordUser1")
                                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        UserDTO userDTO = userService.getUserById(1L);

        Assertions.assertThat(userDTO).isNotNull(); 
        Assertions.assertThat(userDTO.getEmail()).isEqualTo("user1@gmail.com");
    }

    @Test
    public void userService_updateUser_returnUpdatedUserDTO() throws UserNotFoundException {

        User user1 = User.builder()
                                .firstName("user1FirstName")
                                .lastName("user1LastName")
                                .email("user1@gmail.com")
                                .password("passwordUser1")
                                .build();

        UserDTO userDTO = UserDTO.builder()
                                    .firstName("user1updatedFirstName")
                                    .lastName("user1updatedLastName")
                                    .email("user1updated@gmail.com")
                                    .role(Role.ROLE_USER)
                                    .build();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);

        UserDTO savedUserDTO = userService.changeUserData(userDTO, 1L);

        Assertions.assertThat(savedUserDTO).isNotNull(); 
        Assertions.assertThat(savedUserDTO.getEmail()).isEqualTo("user1updated@gmail.com");
        Assertions.assertThat(savedUserDTO.getFirstName()).isEqualTo(user1.getFirstName());
    }

    @Test
    public void userService_deleteUserById_returnUserDTO() throws UserNotFoundException {

        User user1 = User.builder()
                                .firstName("user1FirstName")
                                .lastName("user1LastName")
                                .email("user1@gmail.com")
                                .password("passwordUser1")
                                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        assertAll(() -> userService.deleteUserById(1L));

        
    }
    
}
