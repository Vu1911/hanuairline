package com.se2.hanuairline.service;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.TicketStatus;
import com.se2.hanuairline.model.TicketType;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.payload.TicketPayload;
import com.se2.hanuairline.repository.TicketRepository;
import com.se2.hanuairline.service.aircraft.AircraftSeatService;
import com.se2.hanuairline.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private AircraftSeatService aircraftSeatService;

    public int getNumberOfTicketsByFlightId(Long flight_id){
        return ticketRepository.countByFlight_Id(flight_id);
    }

    public Ticket createTicket (TicketPayload request){
        User user = userService.getUserById(request.getUser_id());
        Flight flight = flightService.getById(request.getFlight_id());
        AircraftSeat aircraftSeat = aircraftSeatService.getAircraftSeatById(request.getAircraftSeat_id());

        // check duplication !!!!!

        if (user == null || flight == null || aircraftSeat == null){
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setAircraftSeat(aircraftSeat);
        ticket.setStatus(TicketStatus.BOOKED);
        ticket.setType(TicketType.valueOf(request.getType()));

        Ticket _ticket = ticketRepository.save(ticket);
        return _ticket;

    }

}
