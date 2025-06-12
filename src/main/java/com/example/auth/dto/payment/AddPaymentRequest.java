package com.example.auth.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class AddPaymentRequest {

	private Long rentalUnitId;

	private BigDecimal amount;

	private String payerName;

	private LocalDate paymentDate;

	private Date monthPaidFor;

	private String paymentMethod;

	private String notes;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getRentalUnitId() {
		return rentalUnitId;
	}

	public void setRentalUnitId(Long rentalUnitId) {
		this.rentalUnitId = rentalUnitId;
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

	public Date getMonthPaidFor() {
		return monthPaidFor;
	}

	public void setMonthPaidFor(Date monthPaidFor) {
		this.monthPaidFor = monthPaidFor;
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
