package com.se2.hanuairline.controller.airport;

import com.se2.hanuairline.model.airport.Airport;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.payload.airport.AirwayPayload;
import com.se2.hanuairline.repository.airport.AirwayRepository;
import com.se2.hanuairline.repository.airport.AirportRepository;
import com.se2.hanuairline.service.airport.AirwayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/airway")
public class AirwayController {

    @Autowired
    private AirwayRepository airwayRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AirwayService airwayService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAirway(@RequestParam(required = false) String departureAircraftName,
                                          @RequestParam(required = false, defaultValue = "false") boolean checkSetPrice,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id,desc") String[] sort) {
        try {
            Page<Airway> airways = airwayService.findAll(page, size, sort, departureAircraftName, checkSetPrice);

            return new ResponseEntity<>(airways, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/getById/{id}")
//    public ResponseEntity<?> getAirwayByArrivalAndDepartureName(@RequestParam(required = true) String arrivalAirport,
//                                                                @RequestParam(required = true) String departureAirport) {
//        Airway airwayData = airwayService.findByArrivalAirportAndDepartureAirport(arrivalAirport, departureAirport);
//
//        return new ResponseEntity<>(airwayData, HttpStatus.OK);
//    }
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/create")
    public ResponseEntity<?> createAirway(@Valid @RequestBody AirwayPayload request) {
        try {
            Airway _airway = airwayService.createAirway(request);

            if(_airway == null){
                return new ResponseEntity<>("Duplicate airway or wrong airport id. Maybe logic error", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(_airway, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // can not update the airway

    // Becareful !!!!!
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteAirway(@PathVariable("id") long id) {
        try {
            if(airwayService.deleteAirway(id)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>("wrong airway id or airway being deployed. Maybe logic error", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findByCities")
    public ResponseEntity<?> findByCities(@RequestParam(required = true) String departureCityName,
                                           @RequestParam(required = true) String arrivalCityName){
        Airway airway = airwayService.findByArrivalCityAndDepartureCity(arrivalCityName, departureCityName);

        if(airway == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(airway, HttpStatus.OK);
    }

}
