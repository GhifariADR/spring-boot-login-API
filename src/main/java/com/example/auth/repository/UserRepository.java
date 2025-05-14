package com.example.auth.repository;

import com.example.auth.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByUsername(String username);



	@Query("SELECT u FROM User u where u.token = :token")
	Optional<User> findByToken(String token);

}
