package com.example.auth.dto.rentalUnit;

import com.example.auth.dto.payment.PaymentResponse;

import java.math.BigDecimal;
import java.util.List;

public class RentalUnitsResponse {

	private Long id;

	private String name;

	private String address;

	private BigDecimal monthlyRent;

	private String status;

	private List<PaymentResponse> payment;

	public RentalUnitsResponse(Long id, String name, String address, BigDecimal monthlyRent, String status, List<PaymentResponse> payment) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.monthlyRent = monthlyRent;
		this.status = status;
		this.payment = payment;
	}

	public RentalUnitsResponse(Long id, String name, String address, BigDecimal monthlyRent, String status) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.monthlyRent = monthlyRent;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getMonthlyRent() {
		return monthlyRent;
	}

	public void setMonthlyRent(BigDecimal monthlyRent) {
		this.monthlyRent = monthlyRent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<PaymentResponse> getPayment() {
		return payment;
	}

	public void setPayment(List<PaymentResponse> payment) {
		this.payment = payment;
	}
}
