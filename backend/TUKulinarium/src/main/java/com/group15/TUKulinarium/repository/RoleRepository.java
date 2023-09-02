package com.group15.TUKulinarium.repository;

import com.group15.TUKulinarium.models.ERole;
import com.group15.TUKulinarium.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole role);
}
