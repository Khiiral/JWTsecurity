package securityproject.securityproject.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import securityproject.securityproject.dto.RequestDTO;
import securityproject.securityproject.exception.RequestNotFoundException;
import securityproject.securityproject.exception.UserNotFoundException;
import securityproject.securityproject.models.Request;
import securityproject.securityproject.models.Role;
import securityproject.securityproject.models.User;
import securityproject.securityproject.repositories.RequestRepository;
import securityproject.securityproject.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    
    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RequestService requestService;

    private User sender;

    private User receiver;

    private UserDetails senderUserDetails;

    private Request request;

    private Request request2;

    private RequestDTO requestDTO;


    @BeforeEach
    public void setUp() {
        sender = User.builder()
                        .firstName("senderFirstName")
                        .lastName("senderLastName")
                        .email("sender@gmail.com")
                        .password("senderpassword")
                        .role(Role.ROLE_USER)
                        .build();

        receiver = User.builder()
                        .firstName("receiverFirstName")
                        .lastName("receiverLastName")
                        .email("receiver@gmail.com")
                        .password("receiverpassword")
                        .build();

        senderUserDetails = sender;

        request = Request.builder()
                        .accepted(false)
                        .newRole("admin")
                        .sender(sender)
                        .receiver(receiver)
                        .build();

        request2 = Request.builder()
                        .accepted(false)
                        .newRole("manager")
                        .sender(sender)
                        .receiver(receiver)
                        .build();

        requestDTO = RequestDTO.builder()
                            .accepted(false)
                            .newRole("admin")
                            .receiverUsername("receiver@gmail.com")
                            .build();
    }

    @Test
    public void requestService_createRequest_returnRequestDTO() throws UserNotFoundException {

        when(userRepository.findByEmail(sender.getUsername())).thenReturn(Optional.ofNullable(sender));
        when(userRepository.findByEmail(receiver.getUsername())).thenReturn(Optional.ofNullable(receiver));

        when(requestRepository.save(Mockito.any(Request.class))).thenReturn(request);

        //Mock SecurityContextHolder behavior
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(senderUserDetails);
        SecurityContextHolder.setContext(securityContext);

        RequestDTO savedRequestDTO = requestService.addRequest(requestDTO);

        User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = currentUser.getUsername();
        String firstName = currentUser.getFirstName();

        Assertions.assertThat(savedRequestDTO).isNotNull();
        Assertions.assertThat(savedRequestDTO.getReceiverUsername()).isEqualTo("receiver@gmail.com");
        assertEquals("sender@gmail.com", username);
        assertEquals("senderFirstName", firstName);
    }

    @Test
    public void requestService_getAllRequestsByUser_returnListOfRequestDTO() {
        when(requestRepository.findAllBySender(sender)).thenReturn(List.of(request, request2));

        List<RequestDTO> requests = requestService.getAllRequestsByCurrentUser(sender);
        requests.get(1).setAccepted(true);

        verify(requestRepository).findAllBySender(sender);

        Assertions.assertThat(requests.get(1).getNewRole()).isEqualTo("manager");
        Assertions.assertThat(requests).isNotNull();
        Assertions.assertThat(requests.size()).isEqualTo(2);
        Assertions.assertThat(requests.get(0).getReceiverUsername()).isEqualTo("receiver@gmail.com");
        Assertions.assertThat(requests.get(0).getNewRole()).isNotEqualTo(requests.get(1).getNewRole());
        Assertions.assertThat(requests.get(1).isAccepted()).isTrue();
    }

    @Test
    public void requestService_getRequest_returnRequestDTO() throws RequestNotFoundException {
        when(requestRepository.findById(1L)).thenReturn(Optional.ofNullable(request));

        RequestDTO requestDTO = requestService.getRequest(1L);

        Assertions.assertThat(requestDTO).isNotNull();
        Assertions.assertThat(requestDTO.getReceiverUsername()).isEqualTo("receiver@gmail.com");
    }

    @Test
    public void requestService_acceptRequest_changeRoleAndAcceptUserRequest() throws RequestNotFoundException {
        when(requestRepository.findById(2L)).thenReturn(Optional.ofNullable(request));

        requestService.acceptRequest(2L);

        assertTrue(request.isAccepted());
        assertEquals(Role.ROLE_ADMIN, sender.getRole());
        Assertions.assertThat(request.isAccepted()).isTrue();
    }
}
