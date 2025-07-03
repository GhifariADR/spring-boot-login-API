package com.example.auth.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("https://auth-fe.up.railway.app")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**") // semua path
//						.allowedOrigins("http://localhost:5173", "https://auth-fe-sandy.vercel.app/", "https://auth-fe.up.railway.app/") // frontend kamu
//						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//						.allowedHeaders("*")
//						.allowCredentials(true);
//			}
//		};
//	}
}
