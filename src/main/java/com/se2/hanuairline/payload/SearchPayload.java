package com.se2.hanuairline.payload;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class SearchPayload {


//    private FlightDirection flightDirection;

    private Long travelClassId;

    private String departureAirportOrCity;

    private String arrivalAirportOrCity;

    private int numberOfTraveler;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd")
    private LocalDate departureTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd")
    private LocalDate arrivalTime;

    // adult children


    public SearchPayload( Long travelClassId, String departureAirportOrCity, String arrivalAirportOrCity, int numberOfTraveler, LocalDate departureTime, LocalDate arrivalTime) {
//        this.flightDirection = flightDirection;
        this.travelClassId = travelClassId;
        this.departureAirportOrCity = departureAirportOrCity;
        this.arrivalAirportOrCity = arrivalAirportOrCity;
        this.numberOfTraveler = numberOfTraveler;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public SearchPayload(){

    }

//    public FlightDirection getFlightDirection() {
//        return flightDirection;
//    }
//
//    public void setFlightDirection(FlightDirection flightDirection) {
//        this.flightDirection = flightDirection;
//    }

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

    public LocalDate getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDate departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDate getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDate arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "SearchPayload{" +

                ", travelClassId=" + travelClassId +
                ", departureAirportOrCity='" + departureAirportOrCity + '\'' +
                ", arrivalAirportOrCity='" + arrivalAirportOrCity + '\'' +
                ", numberOfTraveler=" + numberOfTraveler +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}
