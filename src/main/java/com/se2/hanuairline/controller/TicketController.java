package com.se2.hanuairline.controller;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.payload.CartPayload;
import com.se2.hanuairline.payload.TicketPayload;
import com.se2.hanuairline.repository.TicketRepository;
import com.se2.hanuairline.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTicket(@RequestParam(required = false) String tittle) {
        try {
            List<Ticket> tickets = ticketRepository.findAll();

            if(tickets.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tickets, HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable("id") long id) {
        try {
            Optional<Ticket> ticket = ticketRepository.findById(id);

            if(ticket.isPresent()){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(ticket, HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // bo creat ticket đi
//    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
//    @PostMapping("/create")
//    public ResponseEntity<?> createTicket(@Valid @RequestBody TicketPayload request) {
//        try {
//            Ticket ticket = ticketService.createTicket(request);
//            if(ticket == null){
//                return new ResponseEntity<>(HttpStatus.CONFLICT);
//            }
//           return new ResponseEntity<>(ticket, HttpStatus.CREATED);
//        } catch (Exception e) { // fix here
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // no update available
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/delete/{id}")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable("id") Long id, @RequestParam("token") String token) {
        try {
            if (ticketService.deleteTicket(id, token)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getByUserId/{id}")
    public ResponseEntity<?> getByUserId(@PathVariable("id") Long userId){
        List<Ticket> tickets = ticketService.getByUserId(userId);

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/getByFlightId/{id}")
    public ResponseEntity<?> getByFlightId(@PathVariable("id") Long flightId){
        List<Ticket> tickets = ticketService.getByFlightId(flightId);

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PostMapping("/getTicketsPrices")
    public ResponseEntity<?> getTicketsPrices (@RequestBody CartPayload request) throws InvalidInputValueException {
        List<TicketPayload> ticketPayloads = ticketService.getTicketPrice(request);
        return new ResponseEntity<>(ticketPayloads, HttpStatus.OK);
    }
}
