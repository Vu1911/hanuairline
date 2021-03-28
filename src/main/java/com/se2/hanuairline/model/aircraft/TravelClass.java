package com.se2.hanuairline.model.aircraft;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se2.hanuairline.model.PriceByClass;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "travelclass", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        }),
})
public class TravelClass implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "travelClass")
    private Set<SeatsByClass> seatsByClassSet;

    @JsonIgnore
    @OneToMany(mappedBy = "travelClass")
    private Set<AircraftSeat> aircraftSeatSet;

    @JsonIgnore
    @OneToMany(mappedBy = "travelClass")
    private Set<PriceByClass> priceByClasses;

    public TravelClass(Long id, @NotBlank @Size(max = 40) String name, @NotBlank @Size(max = 1000) String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public TravelClass() {

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
