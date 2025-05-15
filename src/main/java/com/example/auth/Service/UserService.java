package com.example.auth.Service;

import com.example.auth.Entity.User;

import java.util.Objects;

public class UserService {

	public static boolean isAdminOrSuperAdmin(User user){
		return !Objects.equals(user.getRole().getName(), "admin")
				|| !Objects.equals(user.getRole().toString(), "superAdmin") ;
	}

}
