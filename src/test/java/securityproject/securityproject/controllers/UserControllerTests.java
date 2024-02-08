package securityproject.securityproject.controllers;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTests {
    
    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username="user1", roles={"USER"})
    public void testUserHomePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome user1, this is the User HomePage")));             
    }

    @Test
    @WithMockUser(username="user1", roles={"USER"})
    public void testGetUserData_SameUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    }

    @Test
    @WithMockUser(username="user1", roles={"USER"})
    public void testGetUserData_DifferentUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}", 2L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
    }
}
