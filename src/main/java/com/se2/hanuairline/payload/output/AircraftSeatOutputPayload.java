package com.se2.hanuairline.payload.output;


import com.se2.hanuairline.model.AircraftSeatStatus;
import com.se2.hanuairline.model.TicketStatus;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class AircraftSeatOutputPayload {
    @Id
    private String id;

    @NotNull
    private Long travelClass_id;

    private AircraftSeatStatus status;

    public AircraftSeatOutputPayload(String id, Long travelClass_id, AircraftSeatStatus status) {
        this.id = id;
        this.travelClass_id = travelClass_id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTravelClass_id() {
        return travelClass_id;
    }

    public void setTravelClass_id(Long travelClass_id) {
        this.travelClass_id = travelClass_id;
    }

    public AircraftSeatStatus getStatus() {
        return status;
    }

    public void setStatus(AircraftSeatStatus status) {
        this.status = status;
    }
}
