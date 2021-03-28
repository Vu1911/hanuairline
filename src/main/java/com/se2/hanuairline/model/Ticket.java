package com.se2.hanuairline.model;

import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.model.airport.Gate;
import com.se2.hanuairline.model.audit.DateAudit;
import com.se2.hanuairline.model.user.User;
import com.sun.istack.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "ticket")
public class Ticket extends DateAudit implements Cloneable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "aircraftseat_id")
    private AircraftSeat aircraftSeat;

    @Nullable
    @Enumerated(EnumType.STRING)
    private TicketType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Transient
    private int totalPrice;

    public Ticket() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public AircraftSeat getAircraftSeat() {
        return aircraftSeat;
    }

    public void setAircraftSeat(AircraftSeat aircraftSeat) {
        this.aircraftSeat = aircraftSeat;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int total_price) {
        this.totalPrice = total_price;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}