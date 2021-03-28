package com.se2.hanuairline.payload.aircraft;

import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.SeatsByClass;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class AircraftTypePayload {
    @NotBlank
    @NotNull(message = "Please provide aircraft name")
    @Size(max = 40)
    private String name;

    @NotBlank
    @NotNull
    private int seat_capacity;

    @NotBlank
    @NotNull
    private int lugage_capacity_kg;

    @NotBlank
    @NotNull
    private int average_velocity;

    public AircraftTypePayload(@NotBlank @Size(max = 40) String name, @NotBlank @NotNull int seat_capacity, @NotBlank @NotNull int lugage_capacity_kg, @NotBlank @NotNull int average_velocity) {
        this.name = name;
        this.seat_capacity = seat_capacity;
        this.lugage_capacity_kg = lugage_capacity_kg;
        this.average_velocity = average_velocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeat_capacity() {
        return seat_capacity;
    }

    public void setSeat_capacity(int seat_capacity) {
        this.seat_capacity = seat_capacity;
    }

    public int getLugage_capacity_kg() {
        return lugage_capacity_kg;
    }

    public void setLugage_capacity_kg(int lugage_capacity_kg) {
        this.lugage_capacity_kg = lugage_capacity_kg;
    }

    public int getAverage_velocity() {
        return average_velocity;
    }

    public void setAverage_velocity(int average_velocity) {
        this.average_velocity = average_velocity;
    }
}
