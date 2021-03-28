package com.se2.hanuairline.repository.aircraft;

import com.se2.hanuairline.model.aircraft.Aircraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    Page<Aircraft> findAll (Pageable pageable);

    Page<Aircraft> findByNameContainingOrStatusOrAircraftType_Id(String name, String status, Long aircraft_type_id, Pageable pageable);
}