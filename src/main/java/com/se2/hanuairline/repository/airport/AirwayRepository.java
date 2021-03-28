package com.se2.hanuairline.repository.airport;

import com.se2.hanuairline.model.airport.Airway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirwayRepository extends JpaRepository<Airway, Long> {
    Page<Airway> findAll (Pageable pageable);

    Airway findDistinctByArrivalAirport_NameAndAndDepartureAirport_Name(String arrivalAirportName, String departureAirportName);

    Optional<Airway> findByArrivalAirport_IdAndDepartureAirport_Id(long arrivalAirport_id, long depatureAirport_id);

    Optional<Airway> findById(Long id);
}
