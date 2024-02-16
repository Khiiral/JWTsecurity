package securityproject.securityproject.repositories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import securityproject.securityproject.models.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository; 

    @BeforeEach
    public void setUp() {
        
        /*User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("user1");
        user1.setLastName(("user1lastname"));
        user1.setEmail("user1@gmail.com");         
        user1.setPassword("123");*/
    }

    //JUnit test for save User operation

    @Test
    public void givenUserObject_whenSave_thenReturnSavedUser() {

        User userTest = User.builder()
                                .firstName("userTestFirstName")
                                .lastName("userTestLastName")
                                .email("usertest@gmail.com")
                                .password("password")
                                .build();

        User savedUser = userRepository.save(userTest);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getEmail()).isEqualTo("usertest@gmail.com");
        //This one fails
        //Assertions.assertThat(savedUser.getPassword()).isEqualTo("123");
    }

    @Test
    public void userRepository_findAll_returnUsers() {

        User userTest1 = User.builder()
                                .firstName("user1FirstName")
                                .lastName("user1LastName")
                                .email("usertest1@gmail.com")
                                .password("passwordUser1")
                                .build();

        User userTest2 = User.builder()
                                .firstName("user2FirstName")
                                .lastName("user2LastName")
                                .email("usertest2@gmail.com")
                                .password("passwordUser2")
                                .build();  
                                
        userRepository.save(userTest1);
        userRepository.save(userTest2);

        List<User> users = userRepository.findAll();

        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void userRepository_findById_returnUserId() {
        User userTest1 = User.builder()
                                .firstName("user1FirstName")
                                .lastName("user1LastName")
                                .email("usertest1@gmail.com")
                                .password("passwordUser1")
                                .build();

        userRepository.save(userTest1);

        User user = userRepository.findById(userTest1.getId()).get();

        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void userRepository_findByEmail_returnUser() {
        User userTest1 = User.builder()
                                .firstName("user1FirstName")
                                .lastName("user1LastName")
                                .email("usertest1@gmail.com")
                                .password("passwordUser1")
                                .build();

        userRepository.save(userTest1);
        User user = userRepository.findByEmail(userTest1.getEmail()).get();

        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void userRepository_updateUser_returnTheUpdatedUser() {
        User userTest1 = User.builder()
                                .firstName("user1FirstName")
                                .lastName("user1LastName")
                                .email("usertest1@gmail.com")
                                .password("passwordUser1")
                                .build();

        userRepository.save(userTest1);

        User user = userRepository.findById(userTest1.getId()).get();
        user.setFirstName("user1UpdatedFirstName");
        user.setEmail("updated@gmail.com");

        User updatedUser = userRepository.save(user);

        Assertions.assertThat(updatedUser.getFirstName()).isEqualTo("user1UpdatedFirstName");
        assertTrue(updatedUser.getEmail().equals("updated@gmail.com"));

    }

    @Test
    public void userRepository_deleteUser_returnUserIsEmpty() {
        User userTest1 = User.builder()
                                .firstName("user1FirstName")
                                .lastName("user1LastName")
                                .email("usertest1@gmail.com")
                                .password("passwordUser1")
                                .build();

        userRepository.save(userTest1);
        userRepository.deleteById(userTest1.getId());

        Optional<User> user = userRepository.findById(userTest1.getId());

        Assertions.assertThat(user).isEmpty();

    }
    
    
}
