package com.example.auth.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {
	private static final String SECRET_KEY = "rahasia";
	private static final long EXPIRATION_TIME = 86400000; // 1 hari (ms)

	public static String generateToken(Long id,String username, String role) {
		return Jwts.builder()
				.setSubject(username)
				.claim("id", id)
				.claim("role",role)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	public static String extractUsername(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();

		return claims.getSubject(); // karena username disimpan di subject
	}

	public static Long extractUserId(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();

		return claims.get("id", Long.class);
	}

	public static boolean isTokenExpired(String token){
		try{
			Claims claims = Jwts.parser()
					.setSigningKey(SECRET_KEY)
					.parseClaimsJws(token)
					.getBody();

			return claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException e){
			return true;
		}

	}
}
