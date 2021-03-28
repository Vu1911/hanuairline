package com.se2.hanuairline.payload.aircraft;

import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.TravelClass;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

public class AircraftSeatPayload {
    @Id
    private String id;

    @NotNull
    private Long travelClass_id;

    @NotNull
    private Long aircraft_id;

    public AircraftSeatPayload(String id, @NotNull Long travelClass_id, @NotNull Long aircraft_id) {
        this.id = id;
        this.travelClass_id = travelClass_id;
        this.aircraft_id = aircraft_id;
    }

    public AircraftSeatPayload(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTravelClass_id() {
        return travelClass_id;
    }

    public void setTravelClass_id(Long travelClass_id) {
        this.travelClass_id = travelClass_id;
    }

    public Long getAircraft_id() {
        return aircraft_id;
    }

    public void setAircraft_id(Long aircraft_id) {
        this.aircraft_id = aircraft_id;
    }
}
