package com.example.auth.dto;

public class ApiResponse<T> {

	private String status;
	private String message;
	private T data;

	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>("success", message, data);
	}

	public static <T> ApiResponse<T> error(String message, T data) {
		return new ApiResponse<>("error", message, data);
	}

	public ApiResponse(String status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
