package com.se2.hanuairline.payload.aircraft;

import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.model.aircraft.SeatsByClass;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class TravelClassPayload {

    @Id
   private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    public TravelClassPayload( Long id,@NotBlank @Size(max = 40) String name, @NotBlank @Size(max = 1000) String description) {
       this.id = id;
        this.name = name;
        this.description = description;

    }
    public TravelClassPayload(){}

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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
