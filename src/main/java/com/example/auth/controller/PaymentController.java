package com.example.auth.controller;

import com.example.auth.Entity.Payment;
import com.example.auth.Entity.RentalUnits;
import com.example.auth.dto.AddPaymentRequest;
import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.PaymentHistoryResponse;
import com.example.auth.repository.PaymentRepository;
import com.example.auth.repository.RentalUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private RentalUnitRepository rentalUnitRepository;

	@PostMapping("/{id}")
	public ResponseEntity<?> getPaymentHistory(@PathVariable Long id){

		List<Payment> paymentList = paymentRepository.findPaymentByUnitId(id);

		if (paymentList.isEmpty()){
			return ResponseEntity.ok(ApiResponse.success("Payment history not found", null));
		}

		List<PaymentHistoryResponse> responses = new ArrayList<>();

		for (Payment p : paymentList) {
			PaymentHistoryResponse response = new PaymentHistoryResponse(
					p.getId(),
					p.getAmount(),
					p.getMonthPaidFor(),
					p.getPayerName(),
					p.getPaymentDate(),
					p.getPaymentMethod(),
					p.getRentalUnit().getId()
			);
			responses.add(response);
		}

		return ResponseEntity.ok(ApiResponse.success("Payment history found", responses));
	}

	@PostMapping("/add-payment")
	public ResponseEntity<?> addPaymentById(@RequestBody AddPaymentRequest request) {
		Optional<RentalUnits> optionalRentalUnits = rentalUnitRepository.findById(request.getRentalUnitId());

		if (!optionalRentalUnits.isPresent()){
			return ResponseEntity.ok(ApiResponse.error("Rental unit id not found", null));
		}

		Payment payment = new Payment();

		payment.setAmount(request.getAmount());
		payment.setPaymentDate(new Date());
		payment.setRentalUnit(optionalRentalUnits.get());
		payment.setMonthPaidFor(request.getMonthPaidFor());
		payment.setNotes(request.getNotes());
		payment.setPayerName(request.getPayerName());
		payment.setPaymentMethod(request.getPaymentMethod());

		paymentRepository.save(payment);

		return ResponseEntity.ok(ApiResponse.success("Payment created", null));

	}
}
