package com.example.auth.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentResponse {

	private Long id;

	private BigDecimal amount;

	private String monthPaidFor;

	private String payerName;

	private LocalDate paymentDate;

	private String paymentMethod;

	private String notes;

	public PaymentResponse(Long id, BigDecimal amount, String monthPaidFor, String payerName, LocalDate paymentDate, String paymentMethod , String notes) {
		this.id = id;
		this.amount = amount;
		this.monthPaidFor = monthPaidFor;
		this.payerName = payerName;
		this.paymentDate = paymentDate;
		this.paymentMethod = paymentMethod;
		this.notes = notes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getMonthPaidFor() {
		return monthPaidFor;
	}

	public void setMonthPaidFor(String monthPaidFor) {
		this.monthPaidFor = monthPaidFor;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
