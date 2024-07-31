package com.esab.springauth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.esab.springauth.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
  UserDetails findByLogin(String login) throws UsernameNotFoundException;
}
