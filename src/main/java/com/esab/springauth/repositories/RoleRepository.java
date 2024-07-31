package com.esab.springauth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esab.springauth.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    boolean existsByName(String name);

}