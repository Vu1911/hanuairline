package com.se2.hanuairline.payload.airport;

import com.se2.hanuairline.model.airport.Airport;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AirwayPayload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long departure_airport_id;

    private Long arrival_airport_id;

    @NotNull
    private int distance_km;

    public AirwayPayload(Long id, Long departure_airport_id, Long arrival_airport_id, @NotBlank @NotNull int distance_km) {
        this.id = id;
        this.departure_airport_id = departure_airport_id;
        this.arrival_airport_id = arrival_airport_id;
        this.distance_km = distance_km;
    }

    public Long getId() {
        return id;
    }

    public AirwayPayload(){

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeparture_airport_id() {
        return departure_airport_id;
    }

    public void setDeparture_airport_id(Long departure_airport_id) {
        this.departure_airport_id = departure_airport_id;
    }

    public Long getArrival_airport_id() {
        return arrival_airport_id;
    }

    public void setArrival_airport_id(Long arrival_airport_id) {
        this.arrival_airport_id = arrival_airport_id;
    }

    public int getDistance_km() {
        return distance_km;
    }

    public void setDistance_km(int distance_km) {
        this.distance_km = distance_km;
    }
}
