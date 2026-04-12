package com.example.studenttaskmanager;

import com.example.studenttaskmanager.entity.Role;
import com.example.studenttaskmanager.entity.User;
import com.example.studenttaskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User normalUser = new User();
        normalUser.setUsername("user1");
        normalUser.setPassword(passwordEncoder.encode("password123"));
        normalUser.setRole(Role.ROLE_USER);
        userRepository.save(normalUser);

        User adminUser = new User();
        adminUser.setUsername("admin1");
        adminUser.setPassword(passwordEncoder.encode("adminpass"));
        adminUser.setRole(Role.ROLE_ADMIN);
        userRepository.save(adminUser);
    }

    @Test
    void accessProtectedEndpointWithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/tasks/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userTryingToAccessAdminEndpoint_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/tasks")
                        .with(httpBasic("user1", "password123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void registerEndpoint_ShouldReturnCreated() throws Exception {
        String requestBody = """
                {
                  "username": "newuser",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }
}