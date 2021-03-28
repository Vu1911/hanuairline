package com.se2.hanuairline.repository.aircraft;

import com.se2.hanuairline.model.aircraft.AircraftType;
import com.se2.hanuairline.model.aircraft.SeatsByClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatsByClassRepository extends JpaRepository<SeatsByClass, Long> {
    Page<SeatsByClass> findAll (Pageable pageable);

    Page<SeatsByClass> findByAircraftType_IdOrTravelClass_Id(Long aircraft_type_id, Long travelclass_id, Pageable pageable);

    Optional<SeatsByClass> findByAircraftType_IdAndTravelClass_Id(Long aircraft_type_id, Long travelclass_id);

    Optional<SeatsByClass> findByAircraftType_IdAndTravelClass_IdAndIdNot(Long aircraft_type_id, Long travelclass_id, Long id);

    List<SeatsByClass> findByAircraftType_Id(Long id);

    void deleteAllByAircraftType(AircraftType aircraftType);

}
