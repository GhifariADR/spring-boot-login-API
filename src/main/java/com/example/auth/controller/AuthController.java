package com.example.auth.controller;

import com.example.auth.Entity.User;
import com.example.auth.model.*;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
		Map<String, String> responseToken = new HashMap<>();
		if (userOpt.isPresent()){
			User user = userOpt.get();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
				if(user.getToken() != null){
					responseToken.put("token", user.getToken());
					System.out.println("User already logged in");
					return ResponseEntity
							.ok(ApiResponse.error("User already logged in", responseToken));
				}
				String token = JwtUtil.generateToken(user.getId(), user.getUsername());
				user.setToken(token);
				userRepository.save(user);
				responseToken.put("token", token);
				return ResponseEntity.ok(ApiResponse.success("Login successful",responseToken));
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
	public ResponseEntity<?> register(@RequestBody LoginRequest request) {
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			return ResponseEntity.ok(ApiResponse.error("Username already exists",null));
		}
		String hashedPassword = new BCryptPasswordEncoder().encode(request.getPassword());

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(hashedPassword);

		userRepository.save(user);
		return ResponseEntity.ok(ApiResponse.success("User registered successfully",null));
	}



}
