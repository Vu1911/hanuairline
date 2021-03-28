package com.se2.hanuairline.model.aircraft;

import com.se2.hanuairline.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "aircraftSeat")
public class AircraftSeat extends DateAudit implements Cloneable {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name= "travelclass_id", nullable = false)
    private TravelClass travelClass;

    @ManyToOne
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    public AircraftSeat(String id, TravelClass travelClass, Aircraft aircraft) {
        this.id = id;
        this.travelClass = travelClass;
        this.aircraft = aircraft;
    }

    public AircraftSeat() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TravelClass getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(TravelClass travelClass) {
        this.travelClass = travelClass;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
