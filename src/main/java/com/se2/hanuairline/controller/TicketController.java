package com.se2.hanuairline.controller;

import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.payload.TicketPayload;
import com.se2.hanuairline.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    private static final Logger logger = LoggerFactory.getLogger(com.se2.hanuairline.controller.user.UserController.class);

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

    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@RequestBody TicketPayload request) {
        try {
           return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable("id") long id, @RequestBody Ticket ticket) {
        Optional<Ticket> ticketData = ticketRepository.findById(id);

        if (ticketData.isPresent()) {
            try {
                Ticket _ticket = (Ticket) ticket.clone();

                return new ResponseEntity<>(ticketRepository.save(_ticket), HttpStatus.OK);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable("id") long id) {
        try {
            ticketRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
