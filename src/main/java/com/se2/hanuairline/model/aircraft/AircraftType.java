package com.se2.hanuairline.model.aircraft;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se2.hanuairline.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Min;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "aircraft_type", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        }),
})
public class AircraftType extends DateAudit implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "aircraftType")
    private Set<Aircraft> aircraft;

    @Size(max = 40)
    @NotNull(message = "provide aircraft type name")
    private String name;

    @NotNull(message = "provide aircraft type name")
    private int seatCapacity;

    @NotNull
    private int luggageCapacityKg;

    @NotNull
    private int averageVelocity;

    @JsonIgnore
    @OneToMany(mappedBy = "aircraftType")
    private Set<SeatsByClass> seatsByClassSet;

    public AircraftType(@Size(max = 40) @NotNull(message = "provide aircraft type name") String name, @NotNull(message = "provide aircraft type name") int seatCapacity, @NotNull int luggageCapacityKg, @NotNull int averageVelocity) {
        this.name = name;
        this.seatCapacity = seatCapacity;
        this.luggageCapacityKg = luggageCapacityKg;
        this.averageVelocity = averageVelocity;
    }

    public AircraftType(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Aircraft> getAircraft() {
        return aircraft;
    }

    public void setAircraft(Set<Aircraft> aircraft) {
        this.aircraft = aircraft;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public int getLuggageCapacityKg() {
        return luggageCapacityKg;
    }

    public void setLuggageCapacityKg(int luggageCapacityKg) {
        this.luggageCapacityKg = luggageCapacityKg;
    }

    public int getAverageVelocity() {
        return averageVelocity;
    }

    public void setAverageVelocity(int averageVelocity) {
        this.averageVelocity = averageVelocity;
    }

    public Set<SeatsByClass> getSeatsByClassSet() {
        return seatsByClassSet;
    }

    public void setSeatsByClassSet(Set<SeatsByClass> seatsByClassSet) {
        this.seatsByClassSet = seatsByClassSet;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
