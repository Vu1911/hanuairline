package com.se2.hanuairline.repository;

import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    int countByFlight_Id(Long flight_id);

    Optional<Ticket> findTicketByAircraftSeat_IdAndFlight_Id(String aircraftSeatId ,Long Flight_Id);
}