package com.se2.hanuairline.service.aircraft;

import com.se2.hanuairline.exception.NoResultException;
import com.se2.hanuairline.model.aircraft.*;
import com.se2.hanuairline.payload.aircraft.AircraftSeatPayload;
import com.se2.hanuairline.repository.aircraft.AircraftSeatRepository;
import com.se2.hanuairline.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AircraftSeatService {

    @Autowired
    AircraftSeatRepository aircraftSeatRepository;

    @Autowired
    AircraftService aircraftService;

    @Autowired
    TravelClassService travelClassService;

    @Autowired
    SeatsByClassService seatsByClassService;

    public Page<AircraftSeat> findAll (Long aircraft_id, Long travelclass_id, int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);

        if (aircraft_id == null && travelclass_id == null){
            return aircraftSeatRepository.findAll(pagingSort);
        }

        return aircraftSeatRepository.findByAircraft_IdOrTravelClass_Id(aircraft_id, travelclass_id, pagingSort);
    }

    public AircraftSeat getAircraftSeatById(String id){
        Optional<AircraftSeat> aircraftSeatData = aircraftSeatRepository.findById(id);

        if (aircraftSeatData.isPresent()) {
            return aircraftSeatData.get();
        } else {
            return null;
        }
    }

    public boolean createAircraftSeat (Aircraft aircraft){

        AircraftType aircraftType = aircraft.getAircraftType();

        List<SeatsByClass> seatsByClasses = seatsByClassService.findByAircraftTypeId(aircraftType.getId());

        if (seatsByClasses == null){
            return false;
        }

        // for each travel_class
        for (SeatsByClass seatsByClass : seatsByClasses) {
            TravelClass travelClass = seatsByClass.getTravelClass();

            int quantity = seatsByClass.getQuantity();
            String travelClassName = travelClass.getName();

            // generate enough seats for this travel_class
            for (int i = 0; i < quantity; i++){
                String id = travelClassName + i + '-' + aircraft.getId();

                AircraftSeat aircraftSeat = new AircraftSeat();
                aircraftSeat.setId(id);
                aircraftSeat.setAircraft(aircraft);
                aircraftSeat.setTravelClass(travelClass);

                aircraftSeatRepository.save(aircraftSeat);
            }
        }


        return true;
    }

    public boolean deleteAircraftSeatByAircraft(Aircraft aircraft) {
        try{
            aircraftSeatRepository.deleteAircraftSeatsByAircraft(aircraft);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public List<AircraftSeat> findAircraftSeatByAircraftIdAndTravelClassId(Long aircraftId,Long travelClassId) throws NoResultException {
        List<AircraftSeat> aircraftSeats = aircraftSeatRepository.findAircraftSeatByAircraft_IdAndTravelClass_Id(aircraftId,travelClassId);
        if(aircraftSeats.isEmpty()){
            throw new NoResultException("Kh??ng c?? gh??? v???i aircraft_id: "+aircraftId+" v?? travelClass_id :" +travelClassId );
        }
        return aircraftSeats;
    }

    public List<AircraftSeat> getByAircrafId(Long id){
        return aircraftSeatRepository.findByAircraft_Id(id);
    }

    public boolean checkSeatBelongToTheAircraft(String seatId, Long flightId){
        boolean check = false;

        Long aircraftId = null;

        try {
            aircraftId = aircraftService.findAircraftIdByFlightId(flightId);

        } catch (NoResultException e) {
          return false;
        }

        List<AircraftSeat> aircraftSeats =   this.getByAircrafId(aircraftId);
         for(AircraftSeat aircraftSeat : aircraftSeats){
             if (aircraftSeat.getId().equals(seatId)) {
                 check = true;
                 break;
             }
         }


        return check;

    }

}
