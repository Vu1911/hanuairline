package com.se2.hanuairline.service.airport;

import com.se2.hanuairline.model.airport.Airport;
import com.se2.hanuairline.model.airport.Gate;
import com.se2.hanuairline.payload.airport.GatePayload;
import com.se2.hanuairline.repository.airport.GateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GateService {

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private AirportService airportService;

    public List<Gate> getAllGate(){
        return gateRepository.findAll();
    }

    public List<Gate> findGateByAirport (Long airport_id){
        return gateRepository.findByAirport_Id(airport_id);
    }

    public Optional<Gate> getById(long id){
        return gateRepository.findById(id);
    }

    public Gate createGate(GatePayload request){
        Optional<Gate> gateData = gateRepository.findByNameAndAirport_Id(request.getName(), request.getAirport_id());

        if (gateData.isPresent()){
            return null;
        }

        Airport airport = airportService.getById(request.getAirport_id());

        if(airport == null){
            return null;
        }

        Gate gate = new Gate();

        gate.setName(request.getName());
        gate.setAirport(airport);

        System.out.println(request.getName());
        System.out.println(airport);

        Gate _gate = gateRepository.save(gate);

        return _gate;
    }

    public Gate updateGate(Long id, GatePayload request){
        Optional<Gate> gateData = gateRepository.findByNameAndAirport_Id(request.getName(), request.getAirport_id());

        if (gateData.isPresent()){
            return null;
        }

        Optional<Gate> gateD = getById(id);
        Airport airport = airportService.getById(request.getAirport_id());

        if(!gateD.isPresent()){
            return null;
        }

        if(airport != null){
            return null;
        }

        Gate gate = gateD.get();

        gate.setName(request.getName());
        gate.setAirport(airport);

        Gate _gate = gateRepository.save(gate);

        return _gate;
    }

    public boolean deleteGate(Long id){
        Optional<Gate> gateData = gateRepository.findById(id);

        if(!gateData.isPresent()){
            return false;
        }

        Gate gate = gateData.get();

        if(gate.getFlight1().isEmpty() && gate.getFlight2().isEmpty()){
            gateRepository.delete(gate);
            return true;
        }

        return false;
    }
}
