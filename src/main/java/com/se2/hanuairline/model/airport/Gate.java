package com.se2.hanuairline.model.airport;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.audit.DateAudit;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "gate")
public class Gate extends DateAudit implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "airport_id")
    private Airport airport;

    @NaturalId
    @NotBlank
    @NotNull
    private String name;

    @OneToMany(mappedBy = "departureGate")
    private Set<Flight> flight1;

    @OneToMany(mappedBy = "arrivalGate")
    private Set<Flight> flight2;

    public Gate(Long id, Airport airport, @NotBlank @NotNull String name) {
        this.id = id;
        this.airport = airport;
        this.name = name;
    }

    public Gate() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Flight> getFlight1() {
        return flight1;
    }

    public void setFlight1(Set<Flight> flight1) {
        this.flight1 = flight1;
    }

    public Set<Flight> getFlight2() {
        return flight2;
    }

    public void setFlight2(Set<Flight> flight2) {
        this.flight2 = flight2;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
