package com.se2.hanuairline.repository.airport;

import com.se2.hanuairline.model.airport.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    Page<Airport> findAll (Pageable pageable);

    Page<Airport> findById (Long id, Pageable pageable);

    Page<Airport> findByNameContainingAndCountryContainingAndCityContainingAndStatus (String name, String country, String city, String status, Pageable pageable);

    Optional<Airport> findAirportByName(String airportName);
   List<Airport> findAirportByCity(String cityName);
}
