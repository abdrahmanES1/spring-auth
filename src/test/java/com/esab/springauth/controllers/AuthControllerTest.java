package com.esab.springauth.controllers;

import com.esab.springauth.config.auth.TokenProvider;
import com.esab.springauth.dtos.JwtDto;
import com.esab.springauth.dtos.users.LogInDto;
import com.esab.springauth.entities.User;
import com.esab.springauth.responses.GenericResponse;
import com.esab.springauth.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ShouldReturnJwtToken_WhenCredentialsAreValid() {
        // Arrange
        LogInDto loginDto = new LogInDto("user", "password");
        User mockUser = new User();
        mockUser.setLogin("user");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(tokenProvider.generateAccessToken(mockUser)).thenReturn("mockToken");

        // Act
        ResponseEntity<GenericResponse> response = authController.login(loginDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mockToken", ((JwtDto) response.getBody().getData()).accessToken());
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
    }
}
