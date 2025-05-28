package com.example.auth.repository;

import com.example.auth.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	@Query("SELECT p FROM Payment p WHERE p.rentalUnit.id = :id")
	List<Payment> findPaymentByUnitId(@Param("id") Long id);
}
