package com.se2.hanuairline.repository.airport;

import com.se2.hanuairline.model.airport.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    Page<Airport> findAll (Pageable pageable);

    Page<Airport> findById (Long id, Pageable pageable);

    Page<Airport> findByNameContainingAndCountryContainingAndCityContainingAndStatus (String name, String country, String city, String status, Pageable pageable);
}
