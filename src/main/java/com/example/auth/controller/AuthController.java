package com.example.auth.controller;

import com.example.auth.Entity.Role;
import com.example.auth.Entity.User;
import com.example.auth.Service.EmailService;
import com.example.auth.model.*;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@Tag(name = "Authentication" , description = "API untuk login dan register")
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmailService emailService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		log.info("Login process username = " + loginRequest.getUsername());
		Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
		Map<String, String> responseToken = new HashMap<>();
		if (userOpt.isPresent()){
			User user = userOpt.get();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
				if(user.getToken() != null){
					if (!JwtUtil.isTokenExpired(user.getToken())){
						responseToken.put("token", user.getToken());
						log.info("User already login, token valid");
						return ResponseEntity
								.ok(ApiResponse.error("User already logged in", responseToken));
					} else {
						log.info("token expired, create a new token");
						String newToken = JwtUtil.generateToken(user.getId(), user.getUsername());
						user.setToken(newToken);
						userRepository.save(user);
						responseToken.put("token", newToken);
						return ResponseEntity.ok(ApiResponse.success("Login successful",responseToken));
					}
				} else {
					String newToken = JwtUtil.generateToken(user.getId(), user.getUsername());
					user.setToken(newToken);
					userRepository.save(user);
					responseToken.put("token", newToken);
					return ResponseEntity.ok(ApiResponse.success("Login successful",responseToken));
				}
			}
		}
		return ResponseEntity.ok(ApiResponse.error("Invalid username or password",null));

	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {

		Optional<User> userOpt = userRepository.findByToken(logoutRequest.getToken());

		if (!userOpt.isPresent()){
			return ResponseEntity.ok(ApiResponse.error("Invalid token",null));
		}

		User user = userOpt.get();
		user.setToken(null);
		userRepository.save(user);
		return ResponseEntity.ok(ApiResponse.success("Logout successful",null));

	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegsiterRequest request) {
		log.info("Membuat user: {}", request.getUsername());
		Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
		Optional<Role> roleOpt = roleRepository.findByName("user");

		if (userOpt.isPresent()) {
			return ResponseEntity.ok(ApiResponse.error("Username already exists",null));
		}

		if (userRepository.existsByEmail(request.getEmail())){
			return ResponseEntity.ok(ApiResponse.error("Email already exists",null));
		}

		if (!roleOpt.isPresent()) {
			return ResponseEntity.ok(ApiResponse.error("Role doesn't exists",null));
		}

		String hashedPassword = new BCryptPasswordEncoder().encode(request.getPassword());

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(hashedPassword);
		user.setEmail(request.getEmail());
		user.setRole(roleOpt.get());

		emailService.sendRegistrationEmail(request.getEmail(), request.getUsername());
		log.info("email has been sent");

		userRepository.save(user);
		return ResponseEntity.ok(ApiResponse.success("User registered successfully",null));
	}



}
