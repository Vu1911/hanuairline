package com.se2.hanuairline.repository.aircraft;

import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AircraftSeatRepository extends JpaRepository<AircraftSeat, Long> {
    Page<AircraftSeat> findAll(Pageable pageable);

    Optional<AircraftSeat> findById (String id);

    Page<AircraftSeat> findByAircraft_IdOrTravelClass_Id(Long aircraft_id, Long travelclass_id, Pageable pageable);

    void deleteAircraftSeatsByAircraft(Aircraft aircraft);
}
