package com.se2.hanuairline.repository.aircraft;

import com.se2.hanuairline.model.aircraft.TravelClass;
import com.se2.hanuairline.payload.aircraft.TravelClassPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelClassRepository extends JpaRepository<TravelClass, Long> {
         List<TravelClass> findAll();

 Optional<TravelClass> findByNameAndDescription(String name,String description);
    void deleteById(Long id);

}


