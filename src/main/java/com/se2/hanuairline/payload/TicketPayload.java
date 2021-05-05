package com.se2.hanuairline.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.TicketStatus;
import com.se2.hanuairline.model.TicketType;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.model.user.User;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@ToString
@Data
@JsonIgnoreProperties
public class TicketPayload {
    @Id
    private Long id;

    private Long user_id;

    private Long flight_id;

    private String aircraftSeat_id;

    private Long order_id;

    private TicketStatus status;

    private TicketType type;

    private int totalPrice;

    public TicketPayload(Long user_id, Long flight_id, String aircraftSeat_id ,TicketType ticketType,Long order_id) {

        this.user_id = user_id;
        this.flight_id = flight_id;
        this.aircraftSeat_id = aircraftSeat_id;
        this.type = ticketType;
        this.order_id = order_id;
    }

    public TicketPayload() {
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

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "TicketPayload{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", flight_id=" + flight_id +
                ", aircraftSeat_id='" + aircraftSeat_id + '\'' +
                ", order_id=" + order_id +
                ", status=" + status +
                ", type=" + type +
                '}';
    }

}
