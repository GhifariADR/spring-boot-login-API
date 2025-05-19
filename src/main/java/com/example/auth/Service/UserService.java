package com.example.auth.Service;

import com.example.auth.Entity.User;
import com.example.auth.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

public class UserService {

	public static boolean isAdminOrSuperAdmin(User user){
		return !Objects.equals(user.getRole().getName(), "admin")
				|| !Objects.equals(user.getRole().toString(), "superAdmin") ;
	}

	public static String reNewTokenIfExpired(User user){

		if (user.getToken() != null && JwtUtil.isTokenExpired(user.getToken())){
			return JwtUtil.generateToken(user.getId(), user.getUsername());
		}
		return user.getToken();
	}

}
