package com.example.auth.controller;

import com.example.auth.Service.UserService;
import com.example.auth.dto.ApiResponse;
import com.example.auth.Entity.User;
import com.example.auth.dto.UserPaginationRequest;
import com.example.auth.dto.UserResponse;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User" , description = "API untuk pengelolaan user")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping ("/getAll")
	public ResponseEntity<?> getAllUsers(@RequestBody UserPaginationRequest request) {
		Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(direction, request.getSortBy()));

		String keyword = request.getKeyword() != null ? request.getKeyword() : "";
		Page<User> usersPage = userRepository.searchByUsername(keyword, pageable);

		if (usersPage.isEmpty()){
			return ResponseEntity.ok(ApiResponse.error("No users found",null));
		}
		List<UserResponse> response = new ArrayList<>();

		for (User user : usersPage){
			UserResponse userResponse = new UserResponse(
					user.getId(),
					user.getUsername(),
					user.getEmail(),
					user.getRole().getName()
			);
			response.add(userResponse);
		}

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

	@PostMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id){
		Optional<User> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent()){
			return ResponseEntity.ok(ApiResponse.error("Users not found", null));
		}

		User user = userOptional.get();
		UserResponse response = new UserResponse(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getRole().getName()
		);

		return ResponseEntity.ok(ApiResponse.success("Users found", response));

	}
}
