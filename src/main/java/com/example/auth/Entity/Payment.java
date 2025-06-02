package com.example.auth.Entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "rental_unit_id", nullable = false)
	private RentalUnits rentalUnit;

	@Column(name = "payer_name")
	private String payerName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "payment_date")
	private Date paymentDate;

	private BigDecimal amount;

	@Column(name = "month_paid_for")
	private Date monthPaidFor;

	@Column(name = "payment_method")
	private String paymentMethod;

	private String notes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RentalUnits getRentalUnit() {
		return rentalUnit;
	}

	public void setRentalUnit(RentalUnits rentalUnit) {
		this.rentalUnit = rentalUnit;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
}
