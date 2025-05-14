package com.example.auth.repository;

import com.example.auth.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findById(Long id);

	Optional<Role> findByName(String name);
}
