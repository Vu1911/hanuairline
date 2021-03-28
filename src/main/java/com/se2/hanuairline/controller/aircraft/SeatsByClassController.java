package com.se2.hanuairline.controller.aircraft;

import com.se2.hanuairline.model.aircraft.SeatsByClass;
import com.se2.hanuairline.payload.aircraft.SeatsByClassPayLoad;
import com.se2.hanuairline.service.aircraft.SeatsByClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/seatsByClass")
public class SeatsByClassController {

    @Autowired
    private SeatsByClassService seatsByClassService;

    @GetMapping("/getAll")
    public ResponseEntity<? extends Object> getAllSeatsByClass(@RequestParam(required = false) Long aircraft_type_id,
                                                               @RequestParam(required = false) Long travel_class_id,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "id,desc") String[] sort) {
        try{
            Page<SeatsByClass> seatsByClassesData = seatsByClassService.getAll(aircraft_type_id, travel_class_id, page, size, sort);

            if(seatsByClassesData.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(seatsByClassesData, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getSeatsByClassById(@PathVariable("id") long id) {
        try{
            SeatsByClass seatsByClass = seatsByClassService.getById(id);

            if(seatsByClass == null){
                return new ResponseEntity<>("SeatsByClassController: seat by class not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(seatsByClass, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSeatsByClass(@Valid @RequestBody SeatsByClassPayLoad request) {
        try {
            SeatsByClass seatsByClass = seatsByClassService.createSeatsByClass(request);

            if(seatsByClass == null){
                return new ResponseEntity<>("check aircraft type, travel class id. Maybe logic error", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(seatsByClass, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateSeatsByClass(@PathVariable("id") long id, @Valid @RequestBody SeatsByClassPayLoad request) {
        try {
            SeatsByClass seatsByClass = seatsByClassService.updateSeatsByClass(id, request);

            if (seatsByClass == null){
                return new ResponseEntity<>("check updated fields again. Maybe logic error", HttpStatus.NOT_MODIFIED);
            }

            return new ResponseEntity<>(seatsByClass, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // have to delete aircraft type to delete seats by class

}
