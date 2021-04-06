package com.se2.hanuairline.service;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.TicketStatus;
import com.se2.hanuairline.model.TicketType;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.payload.TicketPayload;
import com.se2.hanuairline.repository.TicketRepository;
import com.se2.hanuairline.security.JwtTokenProvider;
import com.se2.hanuairline.service.aircraft.AircraftSeatService;
import com.se2.hanuairline.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Ticket createTicket (TicketPayload request){
        User user = userService.getUserById(request.getUser_id());
        Flight flight = flightService.getById(request.getFlight_id());
        AircraftSeat aircraftSeat = aircraftSeatService.getAircraftSeatById(request.getAircraftSeat_id());

        // check duplication !!!!!
        Optional<Ticket> check = ticketRepository.findByAircraftSeat_IdAndFlight_Id(request.getAircraftSeat_id(), request.getFlight_id());

        if (user == null || flight == null || aircraftSeat == null || check.isPresent()){
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

    public boolean deleteTicket (Long ticketId, String token){
        try {
            Optional<Ticket> ticket = ticketRepository.findById(ticketId);
            if(!ticket.isPresent()){
                return false;
            }

            Long ownerId = ticket.get().getUser().getId();
            JwtTokenProvider tokenEncoder = new JwtTokenProvider();
            Long userId = tokenEncoder.getUserIdFromJWT(token);

            if(userId != ownerId){
                return false;
            }

            ticketRepository.deleteById(ticketId);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public List<Ticket> getByUserId(Long userId){
        List<Ticket> tickets = ticketRepository.findByUser_Id(userId, Sort.by(Sort.Direction.DESC, "createdAt"));

        return tickets;
    }

    public List<Ticket> getByFlightId(Long flightId){
        List<Ticket> tickets = ticketRepository.findByFlight_Id(flightId);

        return tickets;
    }
}
