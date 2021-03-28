package com.se2.hanuairline.model.aircraft;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "seats_by_class")
public class SeatsByClass implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="travelclass_id", nullable = false )
    private TravelClass travelClass;

    @ManyToOne
    @JoinColumn(name="aircrafttype_id", nullable = false)
    private AircraftType aircraftType;

    @NotBlank
    @NotNull
    private int quantity;

    @NotBlank
    @NotNull
    private int rowQuantity;

    public SeatsByClass(TravelClass travelClass, AircraftType aircraftType, @NotBlank @NotNull int quantity, @NotBlank @NotNull int rowQuantity) {
        this.travelClass = travelClass;
        this.aircraftType = aircraftType;
        this.quantity = quantity;
        this.rowQuantity = rowQuantity;
    }

    public SeatsByClass(){

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

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRowQuantity() {
        return rowQuantity;
    }

    public void setRowQuantity(int rowQuantity) {
        this.rowQuantity = rowQuantity;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}