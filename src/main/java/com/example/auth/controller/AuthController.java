package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.Entity.Role;
import com.example.auth.Entity.User;
import com.example.auth.Service.EmailService;
import com.example.auth.Service.UserService;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

		Date now = new Date();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
		Map<String, String> responseToken = new HashMap<>();

		if (!userOpt.isPresent() || !encoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())){
			log.info("Invalid Credential");
			return ResponseEntity.ok(ApiResponse.error("Invalid Credential", null));

		}

		User user = userOpt.get();
		String token = UserService.reNewTokenIfExpired(user);

		user.setLastLogin(now);
		user.setToken(token);
		userRepository.save(user);

		responseToken.put("token", token);

		return ResponseEntity.ok(ApiResponse.success("Login successful", responseToken));
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

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){

		log.info("forgot password process for" + request.getEmail());
		Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

		if (!userOpt.isPresent()) {
			return ResponseEntity.ok(ApiResponse.error("Email not registered", null));
		}

		User user = userOpt.get();
		String token = UUID.randomUUID().toString();
		Date expiry = new Date(System.currentTimeMillis() + 15 * 60 *1000); //15 menit

		user.setResetToken(token);
		user.setResetTokenExpired(expiry);
		userRepository.save(user);

		String url = "www.frontend.com/reset-password?token" + token;

		emailService.sendResetPasswordEmail(user.getEmail(), user.getUsername(), url);

		return ResponseEntity.ok(ApiResponse.error("Reset password link sent to your email", null));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword (@RequestBody ResetPasswordRequest request){
		log.info("forgot password process for" + request.getToken());

		if (request.getNewPassword() == null || request.getToken() == null){
			log.info("New password or token is null");
			return ResponseEntity.ok(ApiResponse.error("New password or token is null", null));
		}
		Optional<User> optUser = userRepository.findByResetToken(request.getToken());

		if (!optUser.isPresent()){
			log.info("Invalid Token");
			return ResponseEntity.ok(ApiResponse.error("Invalid Token", null));
		}

		User user = optUser.get();

		if (user.getResetTokenExpired() == null || user.getResetTokenExpired().before(new Date())){

			user.setResetToken(null);
			user.setResetTokenExpired(null);
			userRepository.save(user);
			log.info("Token has expired");
			return ResponseEntity.ok(ApiResponse.error("Token has expired", null));
		}

		String hashPassword = new BCryptPasswordEncoder().encode(request.getNewPassword());

		user.setPassword(hashPassword);
		user.setResetToken(null);
		user.setResetTokenExpired(null);
		userRepository.save(user);

		log.info("Password reset successfully");
		return ResponseEntity.ok(ApiResponse.success("Password reset successfully", null));
	}



}
