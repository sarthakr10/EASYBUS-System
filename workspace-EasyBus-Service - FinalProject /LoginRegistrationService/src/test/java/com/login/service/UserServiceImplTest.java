package com.login.service;

import com.login.dto.AuthDto;
import com.login.exception.InvalidCredentialException;
import com.login.exception.UserAlreadyPresentException;
import com.login.model.User;
import com.login.repository.UserRepository;
import com.login.security.Jwtutil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private Jwtutil jwtutil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    // ======= registerUser Tests =======

    @Test
    void testRegisterUser_FirstUser_Success() throws UserAlreadyPresentException {
        User user = createTestUser();

        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Boolean result = userService.registerUser(user);
        assertTrue(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_UserAlreadyExists_ThrowsException() {
        User user = createTestUser();
        User existingUser = createTestUser();
        existingUser.setEmail("test@example.com");

        when(userRepository.findAll()).thenReturn(List.of(existingUser));

        UserAlreadyPresentException ex = assertThrows(UserAlreadyPresentException.class,
                () -> userService.registerUser(user));
        assertEquals("Can't add user. Already exits.", ex.getMessage());
    }

    @Test
    void testRegisterUser_NewEmail_Success() throws UserAlreadyPresentException {
        User user = createTestUser();
        User existingUser = new User();
        existingUser.setEmail("other@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(existingUser));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Boolean result = userService.registerUser(user);
        assertTrue(result);
    }

    // ======= login Tests =======

    @Test
    void testLogin_Success() throws InvalidCredentialException {
        AuthDto authDto = new AuthDto();
        authDto.setEmail("test@example.com");
        authDto.setPassword("pass");

        User user = createTestUser();
        user.setId(123);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(jwtutil.generateToken(user.getName(), user.getRole(), user.getId(), user.getEmail()))
                .thenReturn("mockToken");

        String token = userService.login(authDto);
        assertEquals("mockToken", token);
    }

    @Test
    void testLogin_InvalidAuthentication_ThrowsException() {
        AuthDto authDto = new AuthDto();
        authDto.setEmail("fail@example.com");
        authDto.setPassword("wrong");

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(InvalidCredentialException.class, () -> userService.login(authDto));
    }

    @Test
    void testLogin_AuthenticationNotAuthenticated_ThrowsException() {
        AuthDto authDto = new AuthDto();
        authDto.setEmail("test@example.com");
        authDto.setPassword("pass");

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(InvalidCredentialException.class, () -> userService.login(authDto));
    }

    private User createTestUser() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);
        return user;
    }
}