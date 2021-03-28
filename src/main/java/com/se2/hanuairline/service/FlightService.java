package com.se2.hanuairline.service;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.DiscountEvent;
import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.FlightStatus;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.model.airport.Gate;
import com.se2.hanuairline.payload.FlightPayload;
import com.se2.hanuairline.repository.DiscountEventRepository;
import com.se2.hanuairline.repository.FlightRepository;
import com.se2.hanuairline.repository.aircraft.AircraftRepository;
import com.se2.hanuairline.repository.airport.AirwayRepository;
import com.se2.hanuairline.repository.airport.GateRepository;
import com.se2.hanuairline.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private DiscountEventRepository discountEventRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private AirwayRepository airwayRepository;

    @Autowired
    private GateRepository gateRepository;

    public Page<Flight> getAll(int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);
        return flightRepository.findAll(pagingSort);
    }

    public Flight getById(Long id){
        Optional<Flight> flightData = flightRepository.findById(id);

        if(flightData.isPresent()){
            return flightData.get();
        } else {
            return null;
        }
    }

    public Flight createFlight(FlightPayload request) throws InvalidInputValueException {
        Optional<Aircraft> aircraftData = aircraftRepository.findById(request.getAircraft_id());
        Optional<Airway> airwayData = airwayRepository.findById(request.getAirway_id());
        Optional<Gate> arrival_gateData = gateRepository.findById(request.getArrival_gate_id());
        Optional<Gate> departure_gateData = gateRepository.findById(request.getDeparture_gate_id());

        Flight flight = new Flight();

        if (request.getDiscount_id() != null){
            Optional<DiscountEvent> discountEventData = discountEventRepository.findById(request.getDiscount_id());
            if(!discountEventData.isPresent()){
                throw new InvalidInputValueException("FlightController: discount event not found");
            }

            flight.getDiscount().add(discountEventData.get());
        }

        if(!aircraftData.isPresent()){
            throw new InvalidInputValueException("FlightController: aircraft not found");
        }

        if(!airwayData.isPresent()){
            throw new InvalidInputValueException("FlightController: airway not found");
        }

        if(!arrival_gateData.isPresent()){
            throw new InvalidInputValueException("FlightController: arrival gate not found");
        }

        if(!departure_gateData.isPresent()){
            throw new InvalidInputValueException("FlightController: departure gate not found");
        }

        if(!aircraftData.isPresent()){
            throw new InvalidInputValueException("FlightController: aircraft not found");
        }


        flight.setAircraft(aircraftData.get());
        flight.setAirway(airwayData.get());
        flight.setArrivalGate(arrival_gateData.get());
        flight.setArrivalTime(request.getArrival_time());
        flight.setDepartureGate(departure_gateData.get());
        flight.setDepartureTime(request.getDeparture_time());
        flight.setStatus(FlightStatus.valueOf(request.getStatus()));

        Flight _flight = flightRepository.save(flight);

        return _flight;
    }




}
