package com.example.auth.controller;

import com.example.auth.Entity.Payment;
import com.example.auth.Entity.RentalUnits;
import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.rentalUnit.CreateRentalUnitRequest;
import com.example.auth.dto.payment.PaymentResponse;
import com.example.auth.dto.rentalUnit.RentalUnitsResponse;
import com.example.auth.repository.RentalUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rental-unit")
public class RentalUnitController {

	@Autowired
	private RentalUnitRepository rentalUnitRepository;

	@PostMapping("/getAll")
	public ResponseEntity<?> findAllUnits(){

		List<RentalUnits> listRentalUnit = rentalUnitRepository.findAll();

		if (listRentalUnit.isEmpty()){
			return ResponseEntity.ok(ApiResponse.error("No units found",null));
		}

		List<RentalUnitsResponse> response = new ArrayList<>();

		for (RentalUnits rentalUnits : listRentalUnit){
			RentalUnitsResponse rentalUnitsResponse = new RentalUnitsResponse(
					rentalUnits.getId(),
					rentalUnits.getName(),
					rentalUnits.getAddress(),
					rentalUnits.getMonthlyRent(),
					rentalUnits.getStatus()
			);
			response.add(rentalUnitsResponse);
		}

		return ResponseEntity.ok(ApiResponse.success("Unit found",response));
	}

	@PostMapping("/{id}")
	public ResponseEntity<?> getUnitById(@PathVariable Long id){

		Optional<RentalUnits> rentalUnitOpt = rentalUnitRepository.findById(id);

		if (!rentalUnitOpt.isPresent()) {
			return ResponseEntity.ok(ApiResponse.error("Unit not found",null));
		}

		RentalUnits rentalUnit = rentalUnitOpt.get();
		List<PaymentResponse> paymentResponse = new ArrayList<>();

		for(Payment payment: rentalUnit.getPayment()){
			paymentResponse.add(new PaymentResponse(
					payment.getId(),
					payment.getAmount(),
					payment.getMonthPaidFor(),
					payment.getPayerName(),
					payment.getPaymentDate(),
					payment.getPaymentMethod(),
					payment.getNotes()
			));
		}

		RentalUnitsResponse response = new RentalUnitsResponse(
				rentalUnit.getId(),
				rentalUnit.getName(),
				rentalUnit.getAddress(),
				rentalUnit.getMonthlyRent(),
				rentalUnit.getStatus(),
				paymentResponse
		);

		return ResponseEntity.ok(ApiResponse.success("Unit found",response));
	}

	@PostMapping("/create")
	public ResponseEntity<?> createRentalUnit(@RequestBody CreateRentalUnitRequest request) {

		Optional<RentalUnits> optionalRentalUnits = rentalUnitRepository.findByName(request.getName().toLowerCase());

		if (optionalRentalUnits.isPresent()){
			return ResponseEntity.ok(ApiResponse.error("Unit already exist",null));
		}

		RentalUnits rentalUnits = new RentalUnits();

		rentalUnits.setName(request.getName().toLowerCase());
		rentalUnits.setAddress(request.getAddress());
		rentalUnits.setMonthlyRent(request.getMonthlyRent());
		rentalUnits.setStatus(request.getStatus());

		rentalUnitRepository.save(rentalUnits);

		return ResponseEntity.ok(ApiResponse.success("Unit created",null));
	}

	@PostMapping("/delete-multiple")
	public ResponseEntity<?> deleteAllById(@RequestBody List<Long> ids){
		if (ids.isEmpty()){
			return ResponseEntity.ok(ApiResponse.error("List is empty",null));
		}

		List<RentalUnits> units = rentalUnitRepository.findAllById(ids);
		if (units.isEmpty()){
			return ResponseEntity.ok(ApiResponse.error("No units found for the given IDs",null));
		}

		rentalUnitRepository.deleteAllById(ids);
		return ResponseEntity.ok(ApiResponse.success("Units delete successfully",null));


	}


}
