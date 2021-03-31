package com.se2.hanuairline.service;

import com.se2.hanuairline.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public int getNumberOfTicketsByFlightId(Long flight_id){
        return ticketRepository.countByFlight_Id(flight_id);
    }

}
