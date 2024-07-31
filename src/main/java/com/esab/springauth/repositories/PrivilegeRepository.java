package com.esab.springauth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esab.springauth.entities.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    // Optional<Privilege> findByAuthority(String authority);

    Privilege findByAuthority(String authority);

    boolean existsByAuthority(String authority);
}
