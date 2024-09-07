package dev.doel.TDoh.auth;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ContextConfiguration
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void testAllCanAccessPathRegister() throws Exception {
        mockMvc.perform(post("/api/v1/register"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(get("/api/v1/logout"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUserUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/login"))
                .andExpect(unauthenticated())
                .andExpect(status().isUnauthorized());
    }
    
}