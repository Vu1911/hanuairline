package com.se2.hanuairline.payload;

import java.util.Set;

public class CartPayload {
    private Long flightId;

    private Long userId;

    private Long airwayId;

    private Set<GetTicketPricePayload> getTicketPricePayloads;

    public CartPayload(Long flightId, Long userId, Set<GetTicketPricePayload> getTicketPricePayloads) {
        this.flightId = flightId;
        this.userId = userId;
        this.getTicketPricePayloads = getTicketPricePayloads;
    }

    public Long getFlightId() {
        return flightId;
    }

    public Long getAirwayId() {
        return airwayId;
    }

    public void setAirwayId(Long airwayId) {
        this.airwayId = airwayId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<GetTicketPricePayload> getGetTicketPricePayloads() {
        return getTicketPricePayloads;
    }

    public void setGetTicketPricePayloads(Set<GetTicketPricePayload> getTicketPricePayloads) {
        this.getTicketPricePayloads = getTicketPricePayloads;
    }
}
