package com.se2.hanuairline.payload.airport;

import com.se2.hanuairline.model.airport.Airport;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GatePayload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long airport_id;

    @NotBlank
    @NotNull
    private String name;

    public GatePayload(Long id, Long airport_id, @NotBlank @NotNull String name) {
        this.id = id;
        this.airport_id = airport_id;
        this.name = name;
    }

    public GatePayload(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAirport_id() {
        return airport_id;
    }

    public void setAirport_id(Long airport_id) {
        this.airport_id = airport_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
