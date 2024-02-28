package org.workable.movierama.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.workable.movierama.model.User;
import org.workable.movierama.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository).save(user);
        verify(passwordEncoder).encode("password");
    }

    @Test
    void testFindUserByUsername_UserExists() {
        User user = new User();
        user.setUsername("existingUser");
        when(userRepository.findByUsername("existingUser")).thenReturn(user);

        User foundUser = userService.findUserByUsername("existingUser");

        assertNotNull(foundUser);
        assertEquals("existingUser", foundUser.getUsername());
    }

    @Test
    void testFindUserByUsername_UserDoesNotExist() {
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(null);

        User foundUser = userService.findUserByUsername("nonExistingUser");

        assertNull(foundUser);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        User user = new User();
        user.setUsername("existingUser");
        user.setPassword("password");
        when(userRepository.findByUsername("existingUser")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("existingUser");

        assertNotNull(userDetails);
        assertEquals("existingUser", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonExistingUser");
        });
    }

}
