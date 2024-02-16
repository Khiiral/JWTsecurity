package securityproject.securityproject.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import securityproject.securityproject.dto.JwtAuthenticationResponse;
import securityproject.securityproject.dto.SignInRequest;
import securityproject.securityproject.dto.SignUpRequest;
import securityproject.securityproject.services.AuthenticationService;
import securityproject.securityproject.validator.SignUpRequestValidator;


//@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private SignUpRequestValidator signUpRequestValidator;

    @Autowired
    private ObjectMapper objectMapper;


    //Method to test registration of a user, return the jwt token as a response
    @Test
    public void authenticationController_createUser_returnCreated() throws Exception {
        SignUpRequest validRequest = SignUpRequest.builder()
                                            .firstName("userFirstname")
                                            .lastName("userLastname")
                                            .email("user@gmail.com")
                                            .password("userpassword")
                                            .build();

        JwtAuthenticationResponse response = new JwtAuthenticationResponse("token");

        //signup method behavior
        when(authenticationService.signup(validRequest)).thenReturn(response);
        

        ResultActions attemptedResponse = mockMvc.perform(MockMvcRequestBuilders
                                            .post("/api/v1/register")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(validRequest)));

        attemptedResponse.andExpect(status().isCreated())
                         .andExpect(jsonPath("$.token").value("token"));
    }

    
    //Method to test login
    @Test
    public void authenticationController_login_returnOk() throws JsonProcessingException, Exception {
        //Create the request
        SignInRequest signInRequest = SignInRequest.builder()
                                                .email("user@gmail.com")
                                                .password("userpassword")
                                                .build();

        //Create the jwt token as a response
        JwtAuthenticationResponse response = new JwtAuthenticationResponse("token");

        //Signin method behavior
        when(authenticationService.signin(signInRequest)).thenReturn(response);

        ResultActions attemptedResponse = mockMvc.perform(
                                            MockMvcRequestBuilders.post("/api/v1/login")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(signInRequest)));

        attemptedResponse.andExpect(status().isOk())
                         .andExpect(jsonPath("$.token").value("token"));
    }
}
