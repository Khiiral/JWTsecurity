package securityproject.securityproject.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import securityproject.securityproject.dto.RequestDTO;
import securityproject.securityproject.exception.RequestNotFoundException;
import securityproject.securityproject.exception.UserNotFoundException;
import securityproject.securityproject.models.Request;
import securityproject.securityproject.models.Role;
import securityproject.securityproject.models.User;
import securityproject.securityproject.repositories.RequestRepository;
import securityproject.securityproject.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class RequestService {
    
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public void acceptRequest(Long requestId) throws RequestNotFoundException {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException("Request not found"));
        User sender = request.getSender();
        sender.setRole(Role.ROLE_ADMIN);
        request.setAccepted(true);
        requestRepository.save(request);
        userRepository.save(sender); 
    }

    public RequestDTO addRequest(RequestDTO requestDTO) throws UserNotFoundException {
        String receiverUsername = requestDTO.getReceiverUsername();
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String senderUsername = userDetails.getUsername();

        User sender = userRepository.findByEmail(senderUsername).orElseThrow(() -> new UserNotFoundException("User not found"));
        User receiver = userRepository.findByEmail(receiverUsername).orElseThrow(() -> new UserNotFoundException("User not found"));

        Request request = Request.builder()
                                    .accepted(false)
                                    .newRole(requestDTO.getNewRole())
                                    .sender(sender)
                                    .receiver(receiver)
                                    .build();

        Request savedRequest = requestRepository.save(request);
        return mapToDTO(savedRequest);
    }

    public List<RequestDTO> getAllRequestsByCurrentUser(User user) {
        List<Request> requests = requestRepository.findAllBySender(user);
        List<RequestDTO> requestsDTO = requests.stream()
                                            .map(r -> mapToDTO(r))
                                            .collect(Collectors.toList());
        
        return requestsDTO;
    }


    public RequestDTO mapToDTO(Request request) {

        RequestDTO requestDTO = RequestDTO.builder()
                                    .accepted(false)
                                    .newRole(request.getNewRole())
                                    .receiverUsername(request.getReceiver().getUsername())
                                    .build();
        return requestDTO;
    }

    public RequestDTO getRequest(Long requestId) throws RequestNotFoundException {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException("Request not found"));
        return mapToDTO(request);
    }
}


