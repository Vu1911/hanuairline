package com.se2.hanuairline.repository;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.aircraft.Aircraft;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long>, CrudRepository<Flight,Long>{
    List<Flight> findByAircraft_id(Long aircraft_id);

    Page<Flight> findAll(Pageable pageable);

    // Find all flight distinct by aircraft sorted by the latest departure time
    // has the arrival time less than the given time and the arrival airport id is the given airport
    Optional<Flight> findById(Long id);


    List<Flight> findFlightByAirway_Id(Long id);

//    List<Flight> findFlightByAirway_Id(Long id);

    Optional<Flight> findByArrivalTimeAndDepartureTimeAndArrivalGate_IdAndDepartureGate_Id(Instant arrivalTime, Instant departureTime, Long arrivalGateId, Long departureGateId);

    Flight findDistinctFirstByAircraft(Aircraft aircraft, Sort sort);

}
