package com.se2.hanuairline.controller.aircraft;

import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.payload.aircraft.AircraftPayload;
import com.se2.hanuairline.service.aircraft.AircraftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/aircraft")
public class AircraftController {

    @Autowired
    private AircraftService aircraftService;

    private static final Logger logger = LoggerFactory.getLogger(com.se2.hanuairline.controller.user.UserController.class);

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAirCraft(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) String status,
                                            @RequestParam(required = false) Long aircraft_type_id,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id,desc") String[] sort) {
        try{
            Page<Aircraft> aircraftsData = aircraftService.findAll(name, status, aircraft_type_id, page, size, sort);

            if(aircraftsData.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(aircraftsData, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getAircraftById(@PathVariable("id") long id) {
        Aircraft aircraft = aircraftService.getAircraftById(id);

        if (aircraft != null) {
            return new ResponseEntity<>(aircraft, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAircraft(@RequestBody AircraftPayload request) {
        try {
            Aircraft _aircraft = aircraftService.createAircraft(request);

            if (_aircraft == null) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>(_aircraft, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateAircraft(@PathVariable("id") long id, @RequestBody AircraftPayload request) {
        Aircraft updatedAircraft = aircraftService.updateAircraft(id, request);

        if (updatedAircraft == null){
            return new ResponseEntity<>("Check status, aircraft_id and aircraftType_id carefully. Maybe the logic error.", HttpStatus.NOT_MODIFIED);
        } else {
            return new ResponseEntity<>(updatedAircraft, HttpStatus.OK);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAircraft(@PathVariable("id") long id) {
        boolean checkDelete = aircraftService.deleteAircraft(id);

        if(checkDelete){
            return new ResponseEntity<>("Deactivate success!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Check aircrat_id. Maybe logic error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}