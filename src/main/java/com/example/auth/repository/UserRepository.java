package com.example.auth.repository;

import com.example.auth.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByUsername(String username);

	@Query("SELECT u FROM User u where u.token = :token")
	Optional<User> findByToken(String token);

	@Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%') ) ")
	Page<User> searchByUsername(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.email = :email")
	Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.resetToken = :resetToken")
	Optional<User> findByResetToken(String resetToken);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

}
