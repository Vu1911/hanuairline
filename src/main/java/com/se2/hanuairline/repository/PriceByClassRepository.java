package com.se2.hanuairline.repository;

import com.se2.hanuairline.model.PriceByClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PriceByClassRepository extends JpaRepository<PriceByClass,Long>, CrudRepository<PriceByClass,Long> {

    Optional<PriceByClass> findByTravelClass_IdAndAirway_Id(Long TravelClass_Id,Long Airway_Id);

    Optional<PriceByClass> findById(Long id);

    Optional<PriceByClass> findByAirway_IdAndTravelClass_Name(Long airway_id, String travelclass_name);

    Optional<PriceByClass> findByTravelClass_NameAndAirway_Id(String travelClassName, Long airwayId);
}
