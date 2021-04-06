package com.se2.hanuairline.repository;

import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByAircraftSeat_IdAndFlight_Id(String aircraftSeatId, Long flightId);

    List<Ticket> findByUser_Id(Long userId, Sort sort);

    List<Ticket> findByFlight_Id(Long flightId);

    List<Ticket> findTicketByFlight_Id(Long flightId);


}