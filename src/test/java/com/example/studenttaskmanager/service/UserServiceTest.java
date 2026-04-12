package com.example.studenttaskmanager.service;

import com.example.studenttaskmanager.dto.CreateUserRequest;
import com.example.studenttaskmanager.dto.RegisterRequest;
import com.example.studenttaskmanager.entity.Role;
import com.example.studenttaskmanager.entity.User;
import com.example.studenttaskmanager.exception.ResourceNotFoundException;
import com.example.studenttaskmanager.exception.UsernameAlreadyExistsException;
import com.example.studenttaskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_ShouldSaveUser_WhenUsernameDoesNotExist() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("denisa");
        request.setPassword("password123");

        when(userRepository.existsByUsername("denisa")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("denisa");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.ROLE_USER);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(request);

        assertNotNull(result);
        assertEquals("denisa", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(Role.ROLE_USER, result.getRole());

        verify(userRepository).existsByUsername("denisa");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("denisa");
        request.setPassword("password123");

        when(userRepository.existsByUsername("denisa")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.register(request));

        verify(userRepository).existsByUsername("denisa");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShouldSaveUser_WhenUsernameDoesNotExist() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("admin1");
        request.setPassword("adminpass");
        request.setRole(Role.ROLE_ADMIN);

        when(userRepository.existsByUsername("admin1")).thenReturn(false);
        when(passwordEncoder.encode("adminpass")).thenReturn("encodedAdminPass");

        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setUsername("admin1");
        savedUser.setPassword("encodedAdminPass");
        savedUser.setRole(Role.ROLE_ADMIN);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("admin1", result.getUsername());
        assertEquals(Role.ROLE_ADMIN, result.getRole());
        assertEquals("encodedAdminPass", result.getPassword());

        verify(userRepository).existsByUsername("admin1");
        verify(passwordEncoder).encode("adminpass");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("admin1");
        request.setPassword("adminpass");
        request.setRole(Role.ROLE_ADMIN);

        when(userRepository.existsByUsername("admin1")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(request));

        verify(userRepository).existsByUsername("admin1");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("denisa");
        user.setPassword("encodedPassword");
        user.setRole(Role.ROLE_USER);

        when(userRepository.findByUsername("denisa")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("denisa");

        assertNotNull(result);
        assertEquals("denisa", result.getUsername());
        assertEquals(Role.ROLE_USER, result.getRole());

        verify(userRepository).findByUsername("denisa");
    }

    @Test
    void findByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByUsername("missingUser")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findByUsername("missingUser"));

        verify(userRepository).findByUsername("missingUser");
    }
}