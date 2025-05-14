package com.example.auth.controller;

import com.example.auth.model.ApiResponse;
import com.example.auth.Entity.User;
import com.example.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
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
	public ResponseEntity<?> deleteUser(@PathVariable Long id){
		Optional<User> optUser = userRepository.findById(id);

		if (!optUser.isPresent()){
			return ResponseEntity.ok(ApiResponse.error("Users not found", null));
		}

		userRepository.deleteById(id);
		return ResponseEntity.ok(ApiResponse.success("Users deleted", null));
	}
}
