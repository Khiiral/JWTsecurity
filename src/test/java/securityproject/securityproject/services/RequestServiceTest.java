package securityproject.securityproject.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import securityproject.securityproject.repositories.RequestRepository;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    
    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestService requestService;
}
