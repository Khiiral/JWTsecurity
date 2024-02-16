package securityproject.securityproject.controllers;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import securityproject.securityproject.dto.UserDTO;
import securityproject.securityproject.exception.UserNotFoundException;
import securityproject.securityproject.models.Role;
import securityproject.securityproject.models.User;
import securityproject.securityproject.services.JwtService;
import securityproject.securityproject.services.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration
//@SpringBootTest
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    private UserDTO user1;
    private UserDTO user2;
    private UserDTO user3;

    private User currentUser1;
  
    @BeforeEach
    public void setUp() {
        
        user1 = UserDTO.builder()
                            .firstName("user1Firstname")
                            .lastName("user1Lastname")
                            .email("user1@gmail.com")
                            .role(Role.ROLE_USER)
                            .build();

        user2 = UserDTO.builder()
                            .firstName("user2Firstname")
                            .lastName("user2Lastname")
                            .email("user2@gmail.com")
                            .role(Role.ROLE_USER)
                            .build();

        user3 = UserDTO.builder()
                            .firstName("user3Firstname")
                            .lastName("user3Lastname")
                            .email("admin@gmail.com")
                            .role(Role.ROLE_ADMIN)
                            .build();

       currentUser1 = User.builder()
                            .firstName("user1Firstname") 
                            .lastName("user1Lastname")
                            .email("user1@gmail.com")
                            .password("user1password")
                            .role(Role.ROLE_USER)
                            .build();

    }



    @Test
    @WithMockUser(username = "admin", password = "password", roles = {"ADMIN", "USER"})
    public void testGetAllUsers() throws Exception { 

        //Mock behavior of getAllUsers
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2, user3));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/users")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.size()", Matchers.is(3)))
                        .andExpect(jsonPath("$[0].firstName").value("user1Firstname"))
                        .andExpect(jsonPath("$[0].lastName").value("user1Lastname"))
                        .andExpect(jsonPath("$[0].email").value("user1@gmail.com"))
                        .andExpect(jsonPath("$[0].role").value("ROLE_USER"))
                        .andExpect(jsonPath("$[1].email").value("user2@gmail.com"))
                        .andExpect(jsonPath("$[2].email").value("admin@gmail.com"))
                        .andExpect(jsonPath("$[2].role").value("ROLE_ADMIN"));                  
    }
                                        


    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void testUserHomePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome user1, this is the User HomePage")));             
    }

    @Test
    @WithMockUser(username="user1@gmail.com", password = "user1password", roles={"USER"})
    public void testGetUserData_UserFound_returnOk() throws Exception {

        String jwtToken = jwtService.generateToken("user1@gmail.com");

        when(userService.getUserById(1L)).thenReturn(user1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}", 1L)
        .header("Authorization", "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("user1Firstname"))
        .andExpect(jsonPath("$.lastName").value("user1Lastname"))
        .andExpect(jsonPath("$.email").value("user1@gmail.com"));
        //Add other expectations here
    }

    @Test
    @WithMockUser(username="user1@gmail.com", password = "user1password", roles={"USER"})
    public void testGetUserData_DifferentUser_returnNotFound() throws Exception {

        String jwtToken = jwtService.generateToken("user1@gmail.com");

        when(userService.getUserById(2L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}", 2L)
        .header("Authorization", "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }
}
