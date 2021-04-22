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
//    private int numberOfTicket;
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    // ticket payload
//    List<TicketPayload> ticketPayload;
//    private Long userId;
//    private String aircraftSeatId;
//    private TicketType ticketType;
//    private Long flightId;





}
