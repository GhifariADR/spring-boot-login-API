package com.example.auth.security;

import com.example.auth.Entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		// Skip filter if no Bearer token is present
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String token = authHeader.substring(7);
		log.info("Processing JWT token: {}", token.substring(0, 5) + "...");

		try {
			String username = JwtUtil.extractUsername(token);
			Long userId = JwtUtil.extractUserId(token);

			if (username == null || userId == null) {
				log.warn("Invalid token structure - missing username or userId");
				sendErrorResponse(response, "Invalid token structure");
				return;
			}

			Optional<User> userOpt = userRepository.findById(userId);

			if (!userOpt.isPresent()) {
				log.warn("User not found for username: {}", username);
				sendErrorResponse(response, "User not found");
				return;
			}

			User user = userOpt.get();

			// Validate token against database and expiration
			if (!token.equals(user.getToken()) ) {
				log.warn("Token mismatch for user: {}", username);
				sendErrorResponse(response, "Invalid or expired token");
				return;
			}

			// Create authentication object
			UserDetails userDetails = org.springframework.security.core.userdetails.User
					.withUsername(username)
					.password("") // Password shouldn't be needed after authentication
					.authorities(new ArrayList<>()) // Add proper authorities if needed
					.build();

			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (Exception e) {
			sendErrorResponse(response, "Authentication failed: " + e.getMessage());
//			System.out.println(response +  "Authentication failed: " + e.getMessage());

			return;
		}

		filterChain.doFilter(request, response);

	}
	private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write(
				String.format("{\"status\":\"error\",\"message\":\"%s\"}", message));
	}
}
