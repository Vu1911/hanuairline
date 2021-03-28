package com.se2.hanuairline.payload;

import com.se2.hanuairline.model.DiscountEvent;
import com.se2.hanuairline.model.FlightStatus;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.model.airport.Gate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;

public class FlightPayload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long aircraft_id;

    private Long airway_id;

    @NotNull
    private Instant departure_time;

    private Long departure_gate_id;

    @NotNull
    private Instant arrival_time;

    private Long arrival_gate_id;

    @NotNull
    private String status; ;

    private Long discount_id;

    public FlightPayload(Long id, Long aircraft_id, Long airway_id, @NotBlank @NotNull Instant departure_time, Long departure_gate_id, @NotBlank @NotNull Instant arrival_time, Long arrival_gate_id, @NotBlank @NotNull FlightStatus status, Long discount_id) {
        this.id = id;
        this.aircraft_id = aircraft_id;
        this.airway_id = airway_id;
        this.departure_time = departure_time;
        this.departure_gate_id = departure_gate_id;
        this.arrival_time = arrival_time;
        this.arrival_gate_id = arrival_gate_id;
        this.status = status.toString();
        this.discount_id = discount_id;
    }

    public FlightPayload(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAircraft_id() {
        return aircraft_id;
    }

    public void setAircraft_id(Long aircraft_id) {
        this.aircraft_id = aircraft_id;
    }

    public Long getAirway_id() {
        return airway_id;
    }

    public void setAirway_id(Long airway_id) {
        this.airway_id = airway_id;
    }

    public Instant getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(Instant departure_time) {
        this.departure_time = departure_time;
    }

    public Long getDeparture_gate_id() {
        return departure_gate_id;
    }

    public void setDeparture_gate_id(Long departure_gate_id) {
        this.departure_gate_id = departure_gate_id;
    }

    public Instant getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(Instant arrival_time) {
        this.arrival_time = arrival_time;
    }

    public Long getArrival_gate_id() {
        return arrival_gate_id;
    }

    public void setArrival_gate_id(Long arrival_gate_id) {
        this.arrival_gate_id = arrival_gate_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status.toString();
    }

    public Long getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(Long discount_id) {
        this.discount_id = discount_id;
    }
}
