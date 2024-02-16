package securityproject.securityproject.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import securityproject.securityproject.models.Request;
import securityproject.securityproject.models.User;


@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class RequestRepositoryTest {
    
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
                        
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("user1");
        user1.setLastName(("user1lastname"));
        user1.setEmail("user1@gmail.com");         
        user1.setPassword("123");

        Request request1 = Request.builder()
                            .id(1L)
                            .accepted(false)
                            .newRole("admin")
                            .sender(user1)
                            .build();

                            userRepository.save(user1);
                            requestRepository.save(request1);
    }

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "request", "users", "token");
    }

    @Test
    public void whenFindById_ThenReturnRequest() {
        
        
        Request foundRequest = requestRepository.findById(1L).orElse(null);
        
        assertEquals(foundRequest.getId(), 1L);

    }

    

    
}
