package com.example.auth.controller;

import com.example.auth.Service.UserService;
import com.example.auth.model.ApiResponse;
import com.example.auth.Entity.User;
import com.example.auth.model.UserResponse;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User" , description = "API untuk pengelolaan user")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping ("/getAll")
	public ResponseEntity<?> getAllUsers(
			@PageableDefault(page = 0, size = 3, sort = "username", direction = Sort.Direction.ASC) Pageable pageable
	)
	{
		Page<User> usersPage = userRepository.findAll(pageable);

		if (usersPage.isEmpty()){
			return ResponseEntity.ok(ApiResponse.error("No users found",null));
		}

//		List<UserResponse> response = new ArrayList<>();

		List<UserResponse> response = usersPage.stream()
				.map(user -> new UserResponse(
						user.getId(),
						user.getUsername(),
						user.getEmail(),
						user.getRole().getName()
				))
				.collect(Collectors.toList());

		HashMap<String, Object> responseApi = new HashMap<>();

		responseApi.put("users", response);
		responseApi.put("currentPage", usersPage.getNumber());
		responseApi.put("totalPage",usersPage.getTotalPages());
		responseApi.put("totalItems", usersPage.getTotalElements());


		return ResponseEntity.ok(ApiResponse.success("Users found", responseApi));

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

		if (!currentUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error("Invalid user", null));
		}

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
