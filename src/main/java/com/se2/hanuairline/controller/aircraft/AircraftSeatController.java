package com.se2.hanuairline.controller.aircraft;

import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.service.aircraft.AircraftSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/aircraftSeat")
public class AircraftSeatController {

    @Autowired
    private AircraftSeatService aircraftSeatService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAircraftSeat(@RequestParam(required = false) Long aircraft_id,
                                                @RequestParam(required = false) Long travelclass_id,
                                                @RequestParam(defaultValue =  "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "id,desc") String[] sort) {
        try{
            Page<AircraftSeat> aircraftSeatsData = aircraftSeatService.findAll(aircraft_id, travelclass_id, page, size, sort);

            if(aircraftSeatsData.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(aircraftSeatsData, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getAircraftSeatById(@PathVariable("id") long id) {
        AircraftSeat aircraftSeat = aircraftSeatService.getAircraftSeatById(id);

        if(aircraftSeat != null){
            return new ResponseEntity<>(aircraftSeat, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Check aircraftSeatId. Maybe logic error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
