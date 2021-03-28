package com.se2.hanuairline.repository;

import com.se2.hanuairline.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByAircraft_id(Long aircraft_id);

    Page<Flight> findAll(Pageable pageable);

    // Find all flight distinct by aircraft sorted by the latest departure time
    // has the arrival time less than the given time and the arrival airport id is the given airport
    Optional<Flight> findById(Long id);
}
