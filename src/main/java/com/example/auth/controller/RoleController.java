package com.example.auth.controller;

import com.example.auth.Service.UserService;
import com.example.auth.DTO.ApiResponse;
import com.example.auth.Entity.Role;
import com.example.auth.DTO.RoleRequest;
import com.example.auth.Entity.User;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@Tag(name = "Role" , description = "API untuk pengelolaan role")
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/create")
	public ResponseEntity<?> createRole(@RequestBody RoleRequest request, HttpServletRequest headerRequest){

		String authHeader = headerRequest.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error("Missing or invalid Authorization header", null));
		}

		String token = authHeader.substring(7);
		Long userId = JwtUtil.extractUserId(token);

		Optional<User> userOpt = userRepository.findById(userId);
		if (!userOpt.isPresent()){
			return ResponseEntity.ok(ApiResponse.error("No users found",null));
		}

		if (UserService.isAdminOrSuperAdmin(userOpt.get())){
			return ResponseEntity.ok(ApiResponse.error("Only admin or super admin can create roles",null));
		}

		Optional<Role> roleOpt = roleRepository.findByName(request.getName());
		if (roleOpt.isPresent()){
			return ResponseEntity.ok(ApiResponse.error("Role already exist",null));
		}

		Role role = new Role();

		role.setName(request.getName());
		role.setCreatedBy(userOpt.get());

		roleRepository.save(role);
		return ResponseEntity.ok(ApiResponse.success("Role successfully created ",null));

	}
}
