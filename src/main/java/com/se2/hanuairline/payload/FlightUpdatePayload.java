package com.se2.hanuairline.payload;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class FlightUpdatePayload {

    @NotNull
    private Instant departure_time;

    private Long departure_gate_id;

    @NotNull
    private Instant arrival_time;

    private Long arrival_gate_id;

    public FlightUpdatePayload(Instant departure_time, Long departure_gate_id, Instant arrival_time, Long arrival_gate_id) {
        this.departure_time = departure_time;
        this.departure_gate_id = departure_gate_id;
        this.arrival_time = arrival_time;
        this.arrival_gate_id = arrival_gate_id;
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
}
