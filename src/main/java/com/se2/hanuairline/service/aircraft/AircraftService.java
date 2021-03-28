package com.se2.hanuairline.service.aircraft;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftStatus;
import com.se2.hanuairline.model.aircraft.AircraftType;
import com.se2.hanuairline.payload.aircraft.AircraftPayload;
import com.se2.hanuairline.repository.aircraft.AircraftRepository;
import com.se2.hanuairline.repository.aircraft.AircraftTypeRepository;
import com.se2.hanuairline.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class AircraftService {

    @Autowired
    AircraftRepository aircraftRepository;

    @Autowired
    AircraftTypeRepository aircraftTypeRepository;

    @Autowired
    AircraftSeatService aircraftSeatService;

    public Page<Aircraft> findAll(String name, String status, Long aircraft_type_id, int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);

        Page<Aircraft> aircrafts = null;

        if (name == null && status == null && aircraft_type_id == null){
            return aircraftRepository.findAll(pagingSort);
        }

        return aircraftRepository.findByNameContainingOrStatusOrAircraftType_Id(name, status, aircraft_type_id, pagingSort);
    }

    public Aircraft getAircraftById (Long id) {
        Optional<Aircraft> aircraft = aircraftRepository.findById(id);

        if (aircraft.isPresent()) {
            return aircraft.get();
        } else {
            return null;
        }
    }

    public Aircraft createAircraft (AircraftPayload request){
        Optional<AircraftType> aircraftTypeData = aircraftTypeRepository.findById(request.getAircraft_type_id());

        if (!aircraftTypeData.isPresent()) {
            return null;
        }

        Aircraft aircraft = new Aircraft();
        aircraft.setName(request.getName());
        aircraft.setStatus(AircraftStatus.valueOf(request.getStatus()));
        aircraft.setAircraftType(aircraftTypeData.get());

        Aircraft _aircraft = aircraftRepository
                .save(aircraft);;

        // create AircraftSeat
        if(!aircraftSeatService.createAircraftSeat(aircraft)){
            return null;
        }

        return _aircraft;
    }

    public Aircraft updateAircraft (long id, AircraftPayload request){
        Optional<Aircraft> aircraftData = aircraftRepository.findById(id);
        Optional<AircraftType> aircraftTypeData = aircraftTypeRepository.findById(request.getAircraft_type_id());

        if (!aircraftData.isPresent()) {
            return null;
        }

        if (!aircraftTypeData.isPresent()) {
            return null;
        }

        try {
            Aircraft _aircraft = aircraftData.get();

            if(!_aircraft.getStatus().equals(AircraftStatus.ACTIVATED)){
                _aircraft.setName(request.getName());
                _aircraft.setAircraftType(aircraftTypeData.get());

                Aircraft aircraft = aircraftRepository.save(_aircraft);

                return aircraft;
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    // Actually changing the status to deactivated
    // Total delete can only be done when there is no flight assigned to the aircraft
    public boolean deleteAircraft (long id){
        Optional<Aircraft> aircraftData = aircraftRepository.findById(id);

        if(!aircraftData.isPresent()){
            return false;
        }

        Aircraft _aircraft = aircraftData.get();

        Set<Flight> flights = _aircraft.getFlights();

        // Start logic: Checking whether this aircraft is qualified to be deleted
        if(flights.isEmpty()){
            return aircraftSeatService.deleteAircraftSeatByAircraft(_aircraft);
        }
        // End logic: Checking whether this aircraft is qualified to be deleted

        // Start logic: Checking whether this aircraft is qualified to be deactivated.
        Long count = flights.stream().count();
        Stream<Flight> flightStream = flights.stream();

        Flight lastFlight = flightStream.skip(count - 1).findFirst().get();

        Instant currentTime = Instant.now();

        if (lastFlight.getArrivalTime().compareTo(currentTime) >= 0){
            _aircraft.setStatus(AircraftStatus.DEACTIVATED);
            return true;
        }
        // End logic: Checking whether this aircraft is qualified to be deactivated.

        return false;
    }

    // get all the airplanes which are available at a specific date
    // first get all the flights that are distinct of the aircraft_id and sort it from the latest departure date
    public List<Aircraft> getAvailableAircraftByTime(Instant time){
        List<Aircraft> availableAircraft = null;

        return availableAircraft;
    }

    // check aircraft availability
    //
    public boolean checkAircraftByTimeAndAirport(Aircraft plane, Instant time, Long airport_id){
        return false;
    }

}
