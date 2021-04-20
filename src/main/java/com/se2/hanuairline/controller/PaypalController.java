package com.se2.hanuairline.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.se2.hanuairline.domain.Order;
import com.se2.hanuairline.PaypalService;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.TicketType;
import com.se2.hanuairline.payload.OrderPayload;
import com.se2.hanuairline.payload.TicketPayload;
import com.se2.hanuairline.service.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.se2.hanuairline.model.TicketType.CHILDREN;

// problem : choose correct server , how to send data to sheetdb || database của anh hưng -> không khả quan vì nó không cho lưu
// post lên sheetdb -> cần có dạng
// controller
@Controller
@RequestMapping("/payment")
public class PaypalController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    PaypalService service;

    @Autowired
    private ParsingService parsingService;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";
    // it worked
    public static final String ORDER_API = "https://sheetdb.io/api/v1/xkc9txpjhp07f";
    public static final String TICKET_API = "https://sheetdb.io/api/v1/n51xoi9px5enj";

    @GetMapping("/test")
    public String home() {
//        getDataFromOrderAPI();
//        getDataFromTicketAPI();
//        postDataToOrderAPI();
        postDataToTicketAPI();


        return "home";
    }

    //@ModelAttribute("order")
    @PostMapping("/pay")
    public String payment(@RequestBody OrderPayload orderPayload, @RequestBody List<TicketPayload> ticketPayloadList) {
        try {


            System.out.println(("In pay"));
            System.out.println("Order " + ticketPayloadList.toString());
            Payment payment = service.createPayment(orderPayload.getPrice(), orderPayload.getCurrency(), orderPayload.getMethod(),
                    orderPayload.getIntent(), orderPayload.getDescription(), "http://localhost:8080/" + CANCEL_URL + "/" + orderPayload.getId(),
                    "http://localhost:8080/" + SUCCESS_URL + "/" + orderPayload.getId());
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    // create Order over here but not saved to database
                    // OrderPayload orderPayload = new
                    //this.orderPayload=OrderPayLoad
                    System.out.println("test redirect link :" + "redirect:" + link.getHref());
                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/";
    }

    // if paypal return falied
    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return
                "cancel";
    }

    // if succcess
    @GetMapping("pay/success/")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                //code để xử lý order và gọi create ticket service
                // ? làm thế nào để nó lấy được Id của thằng order

                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }

    private List<Order> getDataFromOrderAPI() {
        ObjectMapper mapper = new ObjectMapper();

        List<Order> result = (List<Order>) parsingService.parse(ORDER_API);
        List<Order> orderList = mapper.convertValue(result, new TypeReference<List<Order>>() {
        });
        for(int i = 0 ; i< orderList.size();i++){
            System.out.println("Each order : "+orderList.get(i));
        }
//        System.out.println(orderList);
        return orderList;

    }

   private List<TicketPayload> getDataFromTicketAPI() {
       ObjectMapper mapper = new ObjectMapper();

       List<TicketPayload> result = (List<TicketPayload>) parsingService.parse(TICKET_API);
       List<TicketPayload> ticketPayloadList = mapper.convertValue(result, new TypeReference<List<TicketPayload>>() {
       });
       for(int i = 0 ; i< ticketPayloadList.size();i++){
           System.out.println("Each ticket : "+ticketPayloadList.get(i));
       }
//        System.out.println(orderList);
       return ticketPayloadList;
   }

   private void postDataToOrderAPI(){
        Order order = new Order(7L,1000000,"VND","paypal","test4","des4");

       Gson json = new Gson();
        String toJson = json.toJson(order);
       toJson = "{\"data\" :["+toJson+"]}";
        System.out.println("response  : "+ toJson);
   HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
       HttpEntity<String> entity  = new HttpEntity<>(toJson, headers);
       ResponseEntity<String> response = this.restTemplate.postForEntity(ORDER_API, entity, String.class);


       if (response.getStatusCode() == HttpStatus.CREATED) {
           System.out.println("Success create order!!");
       } else {
           System.out.println("Fail create order!!");
       }


   }

   private void postDataToTicketAPI(){
        TicketPayload ticketPayload = new TicketPayload(3L,2L,"D1-3",CHILDREN,1L);
       Gson json = new Gson();
       String toJson = json.toJson(ticketPayload);
       toJson = "{"+"\n"+"\"data\" :["+"\n"+toJson+"\n"+"]"+"\n"+"}";
       System.out.println("response  : "+ toJson);
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
       HttpEntity<String> entity  = new HttpEntity<>(toJson, headers);
       ResponseEntity<String> response = this.restTemplate.postForEntity(TICKET_API, entity, String.class);


       if (response.getStatusCode() == HttpStatus.CREATED) {
           System.out.println("Success create ticket!!");
       } else {
           System.out.println("Fail create ticket!!");
       }

   }

}




