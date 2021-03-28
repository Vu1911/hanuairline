package com.se2.hanuairline.payload;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.TicketStatus;
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

    private Long aircraftSeat_id;

    @NotNull
    private String status; ;

    @NotBlank
    @NotNull
    private int total_price;

    public TicketPayload(Long id, Long user_id, Long flight_id, Long aircraftSeat_id, @NotBlank @NotNull TicketStatus status, @NotBlank @NotNull int total_price) {
        this.id = id;
        this.user_id = user_id;
        this.flight_id = flight_id;
        this.aircraftSeat_id = aircraftSeat_id;
        this.total_price = total_price;
    }

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

    public Long getAircraftSeat_id() {
        return aircraftSeat_id;
    }

    public void setAircraftSeat_id(Long aircraftSeat_id) {
        this.aircraftSeat_id = aircraftSeat_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}
