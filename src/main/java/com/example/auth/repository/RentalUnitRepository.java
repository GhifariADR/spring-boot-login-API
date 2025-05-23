package com.example.auth.repository;

import com.example.auth.Entity.RentalUnits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface RentalUnitRepository extends JpaRepository<RentalUnits, Long> {

	Optional<RentalUnits> findByName(String name);
}
