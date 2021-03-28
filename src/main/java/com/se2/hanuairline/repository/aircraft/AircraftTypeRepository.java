package com.se2.hanuairline.repository.aircraft;

import com.se2.hanuairline.model.aircraft.AircraftType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftTypeRepository extends JpaRepository<AircraftType, Long> {
    Page<AircraftType> findAll (Pageable pageable);
}
