package com.login.controller;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.login.dto.AuthDto;
import com.login.model.User;
import com.login.security.Jwtutil;
import com.login.service.UserServiceImpl;
 
public class UserControllerTest {
 
    private MockMvc mockMvc;
 
    @Mock
    private UserServiceImpl userService;
 
    @Mock
    private Jwtutil jwtutil;
 
    @Mock
    private BCryptPasswordEncoder encoder;
 
    @InjectMocks
    private UserController userController;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
 
    @Test
    public void testRegisterUserSuccess() throws Exception {
        User user = new User();
        user.setName("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        when(userService.registerUser(any(User.class))).thenReturn(true);
 
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.Message").value("Register successful"));
 
        verify(userService, times(1)).registerUser(any(User.class));
    }
 
    @Test
    public void testRegisterUserFailure() throws Exception {
        User user = new User();
        user.setName("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        when(userService.registerUser(any(User.class))).thenReturn(false);
 
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Message").value("Boo Failure"));
 
        verify(userService, times(1)).registerUser(any(User.class));
    }
 
    @Test
    public void testLoginUserSuccess() throws Exception {
        AuthDto authDto = new AuthDto();
        authDto.setEmail("test@example.com");
        authDto.setPassword("password");
        String token = "sample-token";
        when(userService.login(any(AuthDto.class))).thenReturn(token);
        when(encoder.encode(anyString())).thenReturn("encoded-password");
 
        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(authDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Message").value("Login successful"))
                .andExpect(jsonPath("$.Token").value(token));
 
        verify(userService, times(1)).login(any(AuthDto.class));
    }
 
    @Test
    public void testValidateToken() throws Exception {
        String token = "sample-token";
        doNothing().when(jwtutil).validateToken(token);
 
        mockMvc.perform(get("/api/user/validate")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Token is valid"));
 
        verify(jwtutil, times(1)).validateToken(token);
    }
}