package com.se2.hanuairline.service.airport;

import com.se2.hanuairline.model.airport.Airport;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.payload.airport.AirwayPayload;
import com.se2.hanuairline.repository.airport.AirwayRepository;
import com.se2.hanuairline.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AirwayService {

    @Autowired
    private AirwayRepository airwayRepository;

    @Autowired
    private AirportService airportService;

    public Page<Airway> findAll (int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);

        return airwayRepository.findAll(pagingSort);
    }

    public Airway findByArrivalAirportAndDepartureAirport (String arrivalAirport, String departureAirport){
        Airway airwayData = airwayRepository.findDistinctByArrivalAirport_NameAndAndDepartureAirport_Name(arrivalAirport, departureAirport);

        return airwayData;
    }

    public Airway createAirway(AirwayPayload request){
        Airport arrival_airport = airportService.getById(request.getArrival_airport_id());
        Airport departure_airport= airportService.getById(request.getDeparture_airport_id());

        if(arrival_airport == null){
            return null;
        }

        if(departure_airport == null){
            return null;
        }

        if(airwayRepository.findByArrivalAirport_IdAndDepartureAirport_Id(arrival_airport.getId(), departure_airport.getId()).isPresent()){
            return null;
        }

        Airway airway = new Airway();

        airway.setArrivalAirport(arrival_airport);
        airway.setDepartureAirport(departure_airport);
        airway.setDistanceKm(request.getDistance_km());

        Airway _airway = airwayRepository.save(airway);

        return _airway;
    }

    // can not have update service

    // airway can only be deleted when there is no flight attached to it
    public boolean deleteAirway(Long id){
        Optional<Airway> airway = airwayRepository.findById(id);

        if(!airway.isPresent()){
            return false;
        }

        if(airway.get().getFlight().isEmpty()){
            airwayRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


}
