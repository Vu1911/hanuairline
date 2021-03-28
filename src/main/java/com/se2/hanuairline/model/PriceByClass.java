package com.se2.hanuairline.model;

import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.TravelClass;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.model.airport.Gate;
import com.se2.hanuairline.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "price_by_class")
public class PriceByClass extends DateAudit implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "travelclass_id")
    private TravelClass travelClass;

    @ManyToOne
    @JoinColumn(name = "airway_id")
    private Airway airway;

    @NotBlank
    @NotNull
    private int price;

    public PriceByClass(Long id, TravelClass travelClass, Airway airway, @NotBlank @NotNull int price) {
        this.id = id;
        this.travelClass = travelClass;
        this.airway = airway;
        this.price = price;
    }

    public PriceByClass() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TravelClass getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(TravelClass travelClass) {
        this.travelClass = travelClass;
    }

    public Airway getAirway() {
        return airway;
    }

    public void setAirway(Airway airway) {
        this.airway = airway;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}