package com.esab.springauth.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.esab.springauth.dtos.users.RegisterDto;
import com.esab.springauth.entities.Role;
import com.esab.springauth.entities.User;
import com.esab.springauth.exceptions.InvalidJwtException;
import com.esab.springauth.repositories.RoleRepository;
import com.esab.springauth.repositories.UserRepository;

import java.util.Optional;

@Service
public class AuthService  {
  @Autowired
  UserRepository repository;

  @Autowired
  RoleRepository roleRepository;

  // @Override
  // public UserDetails loadUserByUsername(String username) {
  //   var user = repository.findByLogin(username);
  //   return user;
  // }

  public UserDetails register(RegisterDto data) throws InvalidJwtException {

    if (repository.findByLogin(data.login()) != null) {
      throw new InvalidJwtException("Username already exists");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

    // new User()
    Optional<Role> ROLE_USER = roleRepository.findByName("ROLE_USER");
    User newUser;
    if (!ROLE_USER.isPresent()) {
      newUser = new User(data.login(), encryptedPassword, Arrays.asList(ROLE_USER.get()));
    }
    newUser = new User(data.login(), encryptedPassword);

    return repository.save(newUser);

  }
}