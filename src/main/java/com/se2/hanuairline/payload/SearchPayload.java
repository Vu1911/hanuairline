package com.se2.hanuairline.payload;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se2.hanuairline.model.FlightDirection;
import com.se2.hanuairline.model.TicketType;

import java.util.Date;

public class SearchPayload {


    private FlightDirection flightDirection;

    private Long travelClassId;

    private String departureAirportOrCity;

    private String arrivalAirportOrCity;

    private int numberOfTraveler;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date departureTime;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date arrivalTime;

    // adult children


    public SearchPayload(FlightDirection flightDirection, Long travelClassId, String departureAirportOrCity, String arrivalAirportOrCity, int numberOfTraveler, Date departureTime, Date arrivalTime) {
        this.flightDirection = flightDirection;
        this.travelClassId = travelClassId;
        this.departureAirportOrCity = departureAirportOrCity;
        this.arrivalAirportOrCity = arrivalAirportOrCity;
        this.numberOfTraveler = numberOfTraveler;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public SearchPayload(){

    }

    public FlightDirection getFlightDirection() {
        return flightDirection;
    }

    public void setFlightDirection(FlightDirection flightDirection) {
        this.flightDirection = flightDirection;
    }

    public Long getTravelClassId() {
        return travelClassId;
    }

    public void setTravelClassId(Long travelClassId) {
        this.travelClassId = travelClassId;
    }

    public String getDepartureAirportOrCity() {
        return departureAirportOrCity;
    }

    public void setDepartureAirportOrCity(String departureAirportOrCity) {
        this.departureAirportOrCity = departureAirportOrCity;
    }

    public String getArrivalAirportOrCity() {
        return arrivalAirportOrCity;
    }

    public void setArrivalAirportOrCity(String arrivalAirportOrCity) {
        this.arrivalAirportOrCity = arrivalAirportOrCity;
    }

    public int getNumberOfTraveler() {
        return numberOfTraveler;
    }

    public void setNumberOfTraveler(int numberOfTraveler) {
        this.numberOfTraveler = numberOfTraveler;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "SearchPayload{" +
                "flightDirection=" + flightDirection +
                ", travelClassId=" + travelClassId +
                ", departureAirportOrCity='" + departureAirportOrCity + '\'' +
                ", arrivalAirportOrCity='" + arrivalAirportOrCity + '\'' +
                ", numberOfTraveler=" + numberOfTraveler +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}
