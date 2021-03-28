package com.se2.hanuairline.controller.airport;

import com.se2.hanuairline.model.airport.Gate;
import com.se2.hanuairline.payload.airport.GatePayload;
import com.se2.hanuairline.service.airport.GateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gate")
public class GateController {

    @Autowired
    private GateService gateService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllGate() {
        try {
            List<Gate> gates = gateService.getAllGate();

            if(!(gates.size() > 0)){
                return new ResponseEntity<>("GateController: no gate", HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(gates, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getByAirportId/{id}")
    public ResponseEntity<?> getByAirportId(@PathVariable Long id){
        List<Gate> gatesData = gateService.findGateByAirport(id);

        return new ResponseEntity<>(gatesData, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getGateById(@PathVariable("id") long id) {
        try {
            Optional<Gate> gateData = gateService.getById(id);

            if(!gateData.isPresent()){
                return new ResponseEntity<>("GateController: gate not found", HttpStatus.NOT_FOUND);
            }

            Gate gate = gateData.get();

            return new ResponseEntity<>(gate, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGate(@Valid @RequestBody GatePayload request) {
        try {
            Gate _gate = gateService.createGate(request);

            if (_gate == null){
                return new ResponseEntity<>("check duplicate or wrong airport id. Maybe logic error",HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(_gate, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateGate(@PathVariable("id") long id, @RequestBody GatePayload request) {
        try {
            Gate _gate = gateService.updateGate(id, request);

            if(_gate == null){
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }

            return new ResponseEntity<>(_gate, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Dont use this
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGate(@PathVariable("id") long id) {
        try {
            if(gateService.deleteGate(id)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>("Delete fail",HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}