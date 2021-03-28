package com.se2.hanuairline.model.aircraft;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "aircraft", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        }),
})
public class Aircraft extends DateAudit implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "aircraftType_id")
    private AircraftType aircraftType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AircraftStatus status;

    @OneToMany(mappedBy = "aircraft", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private Set<AircraftSeat> aircraftSeatSet;

    @OneToMany(mappedBy = "aircraft")
    private Set<Flight> flights;

    public Aircraft(@NotBlank @Size(max = 40) String name, @NotNull AircraftType aircraftType, @NotBlank @NotNull AircraftStatus status) {
        this.name = name;
        this.aircraftType = aircraftType;
        this.status = status;
    }

    public Aircraft(){

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

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public AircraftStatus getStatus() {
        return status;
    }

    public void setStatus(AircraftStatus status) {
        this.status = status;
    }

    public Set<AircraftSeat> getAircraftSeatSet() {
        return aircraftSeatSet;
    }

    public void setAircraftSeatSet(Set<AircraftSeat> aircraftSeatSet) {
        this.aircraftSeatSet = aircraftSeatSet;
    }

    public Set<Flight> getFlights() {
        return flights;
    }

    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
