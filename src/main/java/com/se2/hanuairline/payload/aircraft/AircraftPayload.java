package com.se2.hanuairline.payload.aircraft;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.model.aircraft.AircraftStatus;
import com.se2.hanuairline.model.aircraft.AircraftType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class AircraftPayload {
    @NotBlank
    @Size(max = 40)
    private String name;

    @NotNull
    private Long aircraft_type_id;

    @NotBlank
    @NotNull
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAircraft_type_id() {
        return aircraft_type_id;
    }

    public void setAircraft_type_id(Long aircraftType_id) {
        this.aircraft_type_id = aircraftType_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
