package com.se2.hanuairline.payload;


import com.se2.hanuairline.model.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderPayload {
    private double price;
    private String currency;
    private String method;
    private String intent;
    private String description;
    private int numberOfTicket;

    // ticket payload
//    List<TicketPayload> ticketPayload;
//    private Long userId;
//    private String aircraftSeatId;
//    private TicketType ticketType;
//    private Long flightId;





}
