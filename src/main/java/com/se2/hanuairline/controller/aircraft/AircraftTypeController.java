package com.se2.hanuairline.controller.aircraft;

import com.se2.hanuairline.model.aircraft.AircraftType;
import com.se2.hanuairline.payload.aircraft.AircraftTypePayload;
import com.se2.hanuairline.service.aircraft.AircraftTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/aircraftType")
public class AircraftTypeController {

    @Autowired
    private AircraftTypeService aircraftTypeService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAircraftType(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "id,desc") String[] sort) {
        try {
            Page<AircraftType> aircraftTypes = aircraftTypeService.getAllAircraftType(page, size, sort);

            if(aircraftTypes == null){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(aircraftTypes, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getAircraftTypeById(@PathVariable("id") long id) {
        AircraftType aircraftType = aircraftTypeService.getAircraftTypeById(id);

        if (aircraftType != null) {
            return new ResponseEntity<>(aircraftType, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No aircraft type found!", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAircraftType(@Valid @RequestBody AircraftTypePayload aircraftType) {
        AircraftType newAircraftType = aircraftTypeService.creatAircraftType(aircraftType);

        if(newAircraftType != null){
            return new ResponseEntity<>(newAircraftType, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // no update allowed

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteAircraftType(@PathVariable("id") long id) {
        try {
            aircraftTypeService.deleteAircraftType(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
