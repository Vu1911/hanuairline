package com.se2.hanuairline.service.aircraft;

import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftType;
import com.se2.hanuairline.payload.aircraft.AircraftTypePayload;
import com.se2.hanuairline.repository.aircraft.AircraftTypeRepository;
import com.se2.hanuairline.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AircraftTypeService {

    @Autowired
    private AircraftTypeRepository aircraftTypeRepository;

    @Autowired
    private SeatsByClassService seatsByClassService;

    public Page<AircraftType> getAllAircraftType(int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);
        Page<AircraftType> aircraftTypes = aircraftTypeRepository.findAll(pagingSort);

        if(aircraftTypes.isEmpty()){
            return null;
        }

        return aircraftTypes;
    }

    public AircraftType getAircraftTypeById(Long id){
        Optional<AircraftType> aircraftType = aircraftTypeRepository.findById(id);

        if (aircraftType.isPresent()) {
            return aircraftType.get();
        } else {
            return null;
        }
    }

    public AircraftType creatAircraftType(AircraftTypePayload request){
        try {
            AircraftType clone = new AircraftType();
            clone.setName(request.getName());
            clone.setAverageVelocity(request.getAverage_velocity());
            clone.setLuggageCapacityKg(request.getLugage_capacity_kg());
            clone.setSeatCapacity(clone.getSeatCapacity());

            AircraftType _aircraftType = aircraftTypeRepository
                    .save(clone);
            return _aircraftType;
        } catch (Exception e) {
            return null;
        }
    }

    // Can not update an aircraftType

    // Can only delete when there is no aircraft belongs to this aircraft type
    public boolean deleteAircraftType(long id){
        Optional<AircraftType> aircraftTypeData = aircraftTypeRepository.findById(id);

        if(!aircraftTypeData.isPresent()){
            return true;
        }

        AircraftType aircraftType = aircraftTypeData.get();

        if(!checkNewAircraftType(aircraftType)){
            return false;
        }

        if(!seatsByClassService.deleteByAircraftType(aircraftType)){
            return false;
        }

        aircraftTypeRepository.delete(aircraftType);
        return true;
    }

    protected boolean checkNewAircraftType(AircraftType aircraftType){
        Set<Aircraft> aircrafts = aircraftType.getAircraft();

        if(aircrafts.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}

