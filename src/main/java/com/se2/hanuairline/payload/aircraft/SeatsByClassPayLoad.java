package com.se2.hanuairline.payload.aircraft;

import com.se2.hanuairline.model.aircraft.AircraftType;
import com.se2.hanuairline.model.aircraft.TravelClass;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SeatsByClassPayLoad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long travelClass_id;

    private Long aircraftType_id;

    @NotBlank
    @NotNull
    private int quantity;

    @NotBlank
    @NotNull
    private int rows_quantity;


    public Long getTravelClass_id() {
        return travelClass_id;
    }

    public void setTravelClass_id(Long travelClass_id) {
        this.travelClass_id = travelClass_id;
    }

    public Long getAircraftType_id() {
        return aircraftType_id;
    }

    public void setAircraftType_id(Long aircraftType_id) {
        this.aircraftType_id = aircraftType_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRows_quantity() {
        return rows_quantity;
    }

    public void setRows_quantity(int rows_quantity) {
        this.rows_quantity = rows_quantity;
    }
}
