package com.se2.hanuairline.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.airport.AirportStatus;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.model.airport.Gate;
import com.se2.hanuairline.model.audit.DateAudit;
import com.se2.hanuairline.service.PriceByClassService;
import com.se2.hanuairline.service.TicketService;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "flight")
public class Flight extends DateAudit implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;

    @ManyToOne
    @JoinColumn(name = "airway_id")
    private Airway airway;

    @NotNull
    private Instant departureTime;

    @ManyToOne
    @JoinColumn(name = "departure_gate_id")
    private Gate departureGate;

    @NotNull
    private Instant arrivalTime;

    @ManyToOne
    @JoinColumn(name = "arrival_gate_id")
    private Gate arrivalGate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FlightStatus status; ;

    @JsonIgnore
    @OneToMany(mappedBy = "flight")
    private Set<Ticket> ticket;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinTable(name = "Flight_discount",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    @Nullable
    private Set<DiscountEvent> discount;

    @Transient
    private int minPrice;

    @Transient
    private int remainSlot;

    public Flight(Long id, Aircraft aircraft, Airway airway, @NotNull Instant departureTime, Gate departureGate, @NotNull Instant arrivalTime, Gate arrivalGate, @NotBlank @NotNull FlightStatus status) {
        this.id = id;
        this.aircraft = aircraft;
        this.airway = airway;
        this.departureTime = departureTime;
        this.departureGate = departureGate;
        this.arrivalTime = arrivalTime;
        this.arrivalGate = arrivalGate;
        this.status = status;
    }

    public Flight(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public Airway getAirway() {
        return airway;
    }

    public void setAirway(Airway airway) {
        this.airway = airway;
    }

    public Instant getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Instant departureTime) {
        this.departureTime = departureTime;
    }

    public Gate getDepartureGate() {
        return departureGate;
    }

    public void setDepartureGate(Gate departureGate) {
        this.departureGate = departureGate;
    }

    public Instant getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Gate getArrivalGate() {
        return arrivalGate;
    }

    public void setArrivalGate(Gate arrivalGate) {
        this.arrivalGate = arrivalGate;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public Set<DiscountEvent> getDiscount() {
        return discount;
    }

    public void setDiscount(Set<DiscountEvent> discount) {
        this.discount = discount;
    }

    public Set<Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(Set<Ticket> ticket) {
        this.ticket = ticket;
    }

    public int getMinPrice() {
        Iterator a = this.airway.getPriceByClasses().stream().iterator();
        minPrice = Integer.MAX_VALUE;
        while(a.hasNext()){
            int price = ((PriceByClass) a.next()).getPrice();
            if (price < minPrice){
                minPrice = price;
            }
        }
        return minPrice;
    }

    @Transient
    public int getRemainSlot() {
        System.out.println(this.aircraft.getAircraftType().getSeatCapacity());
        int ticketNumber = 0;
        System.out.println(this.ticket == null);
        if(this.ticket != null){
            ticketNumber = this.ticket.size();
        }
        remainSlot = this.aircraft.getAircraftType().getSeatCapacity() - ticketNumber;
        return remainSlot;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", aircraft=" + aircraft +
                ", airway=" + airway +
                ", departureTime=" + departureTime +
                ", departureGate=" + departureGate +
                ", arrivalTime=" + arrivalTime +
                ", arrivalGate=" + arrivalGate +
                ", status=" + status +
                ", ticket=" + ticket +
                ", discount=" + discount +
                ", minPrice=" + minPrice +
                ", remainSlot=" + remainSlot +
                '}';
    }
}
