package com.se2.hanuairline.payload.airport;

import com.se2.hanuairline.model.airport.AirportStatus;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.model.airport.Gate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class AirportPayload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Size(max = 40)
    private String name;

    @NotBlank
    @NotNull
    @Size(max = 40)
    private String country;

    @NotBlank
    @Size(max = 40)
    private String city;

    @NotBlank
    @NotNull
    private int capacity;

    @NotNull
    private String status; ;



    public AirportPayload(Long id, @NotBlank @NotNull @Size(max = 40) String name, @NotBlank @NotNull @Size(max = 40) String country, @NotBlank @Size(max = 40) String city, @NotBlank @NotNull int capacity, @NotBlank @NotNull AirportStatus status) {
        System.out.println("In constructor");
        this.id = id;
        this.name = name;
        this.country = country;
        this.city = city;
        this.capacity =capacity ;
        System.out.println(this.capacity);

        this.status = status.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
