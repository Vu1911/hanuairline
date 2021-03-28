package com.se2.hanuairline.payload;

import com.se2.hanuairline.model.FlightStatus;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

public class PriceByClassPayload {
    @Id
    private Long id;

    @NotNull
    private Long travelclass_id;
    @NotNull
    private Long airway_id;
    @NotNull
    private int price;

    public PriceByClassPayload(Long id, Long travelclass_id, Long airway_id, int price) {
        this.id = id;
        System.out.println(this.id);
        this.travelclass_id = travelclass_id;
        this.airway_id = airway_id;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTravelclass_id() {
        return travelclass_id;
    }

    public void setTravelclass_id(Long travelclass_id) {
        this.travelclass_id = travelclass_id;
    }

    public Long getAirway_id() {
        return airway_id;
    }

    public void setAirway_id(Long airway_id) {
        this.airway_id = airway_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
