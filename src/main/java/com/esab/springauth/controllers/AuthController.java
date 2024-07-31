package com.esab.springauth.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esab.springauth.config.auth.TokenProvider;
import com.esab.springauth.dtos.JwtDto;
import com.esab.springauth.dtos.users.LogInDto;
import com.esab.springauth.dtos.users.RegisterDto;
import com.esab.springauth.entities.User;
import com.esab.springauth.responses.GenericResponse;
import com.esab.springauth.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private AuthService service;
  @Autowired
  private TokenProvider tokenService;

  @PostMapping("/register")
  public ResponseEntity<?> signUp(@RequestBody @Valid RegisterDto data) {
    service.register(data);
    return ResponseEntity.status(HttpStatus.CREATED).build();

  }

  @PostMapping("/login")
  public ResponseEntity<GenericResponse> login(@RequestBody @Valid LogInDto data) {

    var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

    var authUser = authenticationManager.authenticate(usernamePassword);

    var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());

    GenericResponse response = GenericResponse.builder()
        .data(new JwtDto(accessToken))
        .message("token retrived su")
        .status(HttpStatus.OK.value())
        .build();

    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/me")
  public UserDetails getMe() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      return (UserDetails) authentication.getPrincipal();
    } else {
      throw new RuntimeException("No authenticated user found");
    }
  }

}