package com.se2.hanuairline.controller.airport;

import com.se2.hanuairline.model.airport.Airport;
import com.se2.hanuairline.payload.airport.AirportPayload;
import com.se2.hanuairline.repository.airport.AirportRepository;
import com.se2.hanuairline.service.airport.AirportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/airport")
public class AirportController {

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AirportService airportService;

    private static final Logger logger = LoggerFactory.getLogger(com.se2.hanuairline.controller.user.UserController.class);

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAirport(@RequestParam(required = false) Long id,
                                       @RequestParam(required = false, defaultValue = "_") String name,
                                       @RequestParam(required = false, defaultValue = "_") String country,
                                       @RequestParam(required = false, defaultValue = "_") String city,
                                       @RequestParam(required = false, defaultValue = "_") String status,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id,desc") String[] sort) {
        try {
            Page<Airport> airportData = airportService.findAll(id, name, country, city, status, page, size, sort);

            return new ResponseEntity<>(airportData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getAirportById(@PathVariable("id") long id) {
        Airport airport = airportService.getById(id);

        if (airport != null) {
            return new ResponseEntity<>(airport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAirport(@Valid @RequestBody AirportPayload request) {
        try {
            Airport _airport = airportService.createAirport(request);
            return new ResponseEntity<>(_airport, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateAirport(@PathVariable("id") long id, @RequestBody AirportPayload request) {
        Airport airport = airportService.updateAirport(id, request);

        if (airport != null) {
            return new ResponseEntity<>(airport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteAirport(@PathVariable("id") long id) {
        try {
            if(airportService.deleteAirport(id)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}