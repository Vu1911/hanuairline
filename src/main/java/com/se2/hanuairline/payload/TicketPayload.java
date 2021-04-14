package com.se2.hanuairline.payload;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.TicketStatus;
import com.se2.hanuairline.model.TicketType;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TicketPayload {
    @Id
    private Long id;

    private Long user_id;

    private Long flight_id;

    private String aircraftSeat_id;

    private TicketStatus status;

    private TicketType type;


    public TicketPayload(Long user_id, Long flight_id, String aircraftSeat_id , TicketType ticketType) {

        this.user_id = user_id;
        this.flight_id = flight_id;
        this.aircraftSeat_id = aircraftSeat_id;
        this.type = ticketType;
    }

//    public TicketPayload(Long user_id, Long flight_id, String aircraftSeat_id, String status) {
//        this.user_id = user_id;
//        this.flight_id = flight_id;
//        this.aircraftSeat_id = aircraftSeat_id;
//        this.status = status;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(Long flight_id) {
        this.flight_id = flight_id;
    }

    public String getAircraftSeat_id() {
        return aircraftSeat_id;
    }

    public void setAircraftSeat_id(String aircraftSeat_id) {
        this.aircraftSeat_id = aircraftSeat_id;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }
}
