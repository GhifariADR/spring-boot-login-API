package com.example.auth.controller;

import com.example.auth.Service.UserService;
import com.example.auth.model.ApiResponse;
import com.example.auth.Entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User" , description = "API untuk pengelolaan user")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/getAll")
	public ResponseEntity<?> getAllUsers(){
		List<User> users = userRepository.findAll();
		if (users.isEmpty()){
			return ResponseEntity.ok(ApiResponse.error("No users found",null));
		}
		return ResponseEntity.ok(ApiResponse.success("Users found", users));

	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpServletRequest headerRequest){

		String authHeader = headerRequest.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error("Missing or invalid Authorization header", null));
		}

		String token = authHeader.substring(7);
		Long currentUserId = JwtUtil.extractUserId(token);

		Optional<User> currentUser = userRepository.findById(currentUserId);
		if(UserService.isAdminOrSuperAdmin(currentUser.get())){
			return ResponseEntity.ok(ApiResponse.error("Only admin or super admin can delete user",null));
		}


		Optional<User> optUser = userRepository.findById(id);

		if (!optUser.isPresent()){
			return ResponseEntity.ok(ApiResponse.error("Users not found", null));
		}

		userRepository.deleteById(id);
		return ResponseEntity.ok(ApiResponse.success("Users deleted", null));
	}
}
