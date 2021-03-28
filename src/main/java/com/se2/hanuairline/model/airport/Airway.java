package com.se2.hanuairline.model.airport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.PriceByClass;
import com.se2.hanuairline.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "airway")
public class Airway extends DateAudit implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrivalAirport;

    @NotNull
    private int distanceKm;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "airway")
    private Set<Flight> flight;

    @OneToMany(mappedBy = "airway")
    @JsonIgnore
    private Set<PriceByClass> priceByClasses;

    public Airway(Long id, Airport departure_airport, Airport arrival_airport, @NotBlank @NotNull int distance_km) {
        this.id = id;
        this.departureAirport = departure_airport;
        this.arrivalAirport = arrival_airport;
        this.distanceKm = distance_km;
    }

    public Airway() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public int getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(int distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Set<Flight> getFlight() {
        return flight;
    }

    public void setFlight(Set<Flight> flight) {
        this.flight = flight;
    }

    public Set<PriceByClass> getPriceByClasses() {
        return priceByClasses;
    }

    public void setPriceByClasses(Set<PriceByClass> priceByClasses) {
        this.priceByClasses = priceByClasses;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
