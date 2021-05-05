package com.se2.hanuairline.payload;

import com.se2.hanuairline.model.TicketType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class GetTicketPricePayload {

    private Long airwayId;

    private String seatId;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    public GetTicketPricePayload(Long airwayId, String seatId, TicketType ticketType) {
        this.airwayId = airwayId;
        this.seatId = seatId;
        this.ticketType = ticketType;
    }

    public Long getAirwayId() {
        return airwayId;
    }

    public void setAirwayId(Long airwayId) {
        this.airwayId = airwayId;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }
}
