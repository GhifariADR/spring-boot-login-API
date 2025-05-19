package com.example.auth.Service;

import com.example.auth.Entity.User;
import com.example.auth.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

@Slf4j
public class UserService {

	public static boolean isAdminOrSuperAdmin(User user){
		return !Objects.equals(user.getRole().getName(), "admin")
				|| !Objects.equals(user.getRole().toString(), "superAdmin") ;
	}

	public static String reNewTokenIfExpired(User user){

		if (user.getToken() != null && JwtUtil.isTokenExpired(user.getToken())){
			log.info("Token expired, renew token");
			return JwtUtil.generateToken(user.getId(), user.getUsername());

		} else if (user.getToken() == null) {
			log.info("generate new token");
			return JwtUtil.generateToken(user.getId(), user.getUsername());
		}
		log.info("use old token");
		return user.getToken();
	}

}
