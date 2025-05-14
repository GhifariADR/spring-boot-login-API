package com.example.auth.controller;

import com.example.auth.model.ApiResponse;
import com.example.auth.Entity.Role;
import com.example.auth.model.RoleRequest;
import com.example.auth.Entity.User;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/create")
	public ResponseEntity<?> createRole(
			@RequestHeader("Authorization") String authHeader, @RequestBody RoleRequest request){

		String token = authHeader.replace("Bearer ", "");
		Long userId = JwtUtil.extractUserId(token);

		Optional<User> userOpt = userRepository.findById(userId);
		if (!userOpt.isPresent()){
			return ResponseEntity.ok(ApiResponse.error("No users found",null));
		}

		Optional<Role> roleOpt = roleRepository.findByName(request.getName());
		if (roleOpt.isPresent()){
			return ResponseEntity.ok(ApiResponse.error("Role already exist",null));
		}

		Role role = new Role();

		role.setName(request.getName());
		role.setCreatedBy(userOpt.get());

		roleRepository.save(role);
		return ResponseEntity.ok(ApiResponse.error("Role successfully created ",null));

	}
}
