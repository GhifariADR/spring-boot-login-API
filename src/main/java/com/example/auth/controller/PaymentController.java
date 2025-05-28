package com.example.auth.controller;

import com.example.auth.Entity.Payment;
import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.PaymentHistoryResponse;
import com.example.auth.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private PaymentRepository paymentRepository;

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
}
