package com.se2.hanuairline.model.airport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se2.hanuairline.model.audit.DateAudit;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "airport", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        }),
})
public class Airport extends DateAudit implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
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

    @NotNull
    private int capacity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AirportStatus status;

    @OneToMany(mappedBy = "airport")
    @JsonIgnore
    private Set<Gate> gates;


    @OneToMany(mappedBy = "departureAirport")
    @JsonIgnore
    private Set<Airway> airway1;


    @OneToMany(mappedBy = "arrivalAirport")
    @JsonIgnore
    private Set<Airway> airway2;

    public Airport(@NotBlank @NotNull @Size(max = 40) String name, @NotBlank @NotNull @Size(max = 40) String country, @NotBlank @Size(max = 40) String city, @NotNull int capacity, @NotBlank @NotNull AirportStatus status) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.capacity = capacity;
        this.status = status;
    }

    public Airport(){

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

    public AirportStatus getStatus() {
        return status;
    }

    public void setStatus(AirportStatus status) {
        this.status = status;
    }

    @JsonIgnore
    public Set<Gate> getGates() {
        return gates;
    }

    public void setGates(Set<Gate> gates) {
        this.gates = gates;
    }

    @JsonIgnore
    public Set<Airway> getAirway1() {
        return airway1;
    }

    public void setAirway1(Set<Airway> airway1) {
        this.airway1 = airway1;
    }

    @JsonIgnore
    public Set<Airway> getAirway2() {
        return airway2;
    }

    public void setAirway2(Set<Airway> airway2) {
        this.airway2 = airway2;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Airport{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", capacity=" + capacity +
                ", status='" + status + '\'' +
                ", gates=" + gates +
                ", airway1=" + airway1 +
                ", airway2=" + airway2 +
                '}';
    }
}
