package securityproject.securityproject.repositories;


import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import securityproject.securityproject.models.Request;
import securityproject.securityproject.models.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RequestRepositoryIntegrationTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void requestRepository_saveRequest_returnRequestObject() {

        User sender = User.builder()
                        .firstName("senderFirstName")
                        .lastName("senderLastName")
                        .email("sender@gmail.com")
                        .password("senderpassword")
                        .build();

        
        User receiver = User.builder()
                        .firstName("receiverFirstName")
                        .lastName("receiverLastName")
                        .email("receiver@gmail.com")
                        .password("receiverpassword")
                        .build();
    

        Request request = Request.builder()
                            .accepted(false)
                            .newRole("admin")
                            .sender(sender)
                            .receiver(receiver)
                            .build();

        userRepository.save(sender);
        userRepository.save(receiver);
        Request savedRequest = requestRepository.save(request);

        Assertions.assertThat(savedRequest).isNotNull();
        Assertions.assertThat(savedRequest.getReceiver().getEmail()).isEqualTo("receiver@gmail.com");
    }

    @Test
    public void requestRepository_getAll_returnAllTheRequests() {

        User sender1 = User.builder()
                        .firstName("sender1FirstName")
                        .lastName("sender1LastName")
                        .email("sender1@gmail.com")
                        .password("sender1password")
                        .build();

        User sender2 = User.builder()
                        .firstName("sender2FirstName")
                        .lastName("sender2LastName")
                        .email("sender2@gmail.com")
                        .password("sender2password")
                        .build();

        
        User receiver = User.builder()
                        .firstName("receiverFirstName")
                        .lastName("receiverLastName")
                        .email("receiver@gmail.com")
                        .password("receiverpassword")
                        .build();
    

        Request request1 = Request.builder()
                            .accepted(false)
                            .newRole("admin")
                            .sender(sender1)
                            .receiver(receiver)
                            .build();

        Request request2 = Request.builder()
                            .accepted(true)
                            .newRole("manager")
                            .sender(sender1)
                            .receiver(receiver)
                            .build();

        //Save the users
        userRepository.save(sender1);
        userRepository.save(sender2);
        userRepository.save(receiver);

        //Save the requests
        requestRepository.save(request1);
        requestRepository.save(request2);

        List<Request> requests = requestRepository.findAll();

        Assertions.assertThat(requests.size()).isEqualTo(2);
        Assertions.assertThat(requests.get(1).isAccepted()).isTrue();

    }
    
}
