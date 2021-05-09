package com.se2.hanuairline.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.se2.hanuairline.domain.Order;
import com.se2.hanuairline.PaypalService;
import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.TicketType;
import com.se2.hanuairline.payload.OrderPayload;
import com.se2.hanuairline.payload.TicketPayload;
import com.se2.hanuairline.service.ParsingService;
import com.se2.hanuairline.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.se2.hanuairline.model.TicketType.CHILDREN;

// problem : choose correct server , how to send data to sheetdb || database của anh hưng -> không khả quan vì nó không cho lưu
// post lên sheetdb -> cần có dạng
// controller
@RestController
@RequestMapping("/payment")
public class PaypalController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    PaypalService service;

    @Autowired
    TicketService ticketService;
    @Autowired
    private ParsingService parsingService;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";
    // it worked
    public static final String ORDER_API = "https://sheetdb.io/api/v1/02doy65jmii53";
    public static final String TICKET_API = "https://sheetdb.io/api/v1/zrmouj40lgkwf";

    @GetMapping("/test")
    public String home() {
//        getDataFromOrderAPI();
//        getDataFromTicketAPI();
//        postDataToOrderAPI();
//        postDataToTicketAPI();


        return "home";
    }

    //@ModelAttribute("order"), List<TicketPayload> ticketPayloadList
    @PostMapping("/pay")
    public String payment(@RequestBody OrderPayload orderPayload) {
        try {
            /*
             lấy order lưu trên api -> tạo order mới với id mới
             Dựa vào các ticketPayload -> lặp qua , gán orderId
             đẩy order,ticketPayload lên api

             */


            System.out.println(("In pay"));
            System.out.println("Order " + orderPayload.toString());
            List<Order> orderList = this.getDataFromOrderAPI();

            Order order = this.postDataToOrderAPI(orderPayload);
            for(TicketPayload ticketPayload : orderPayload.getTicketPayloads()){
                ticketPayload.setOrder_id(order.getOrderId());
                this.postDataToTicketAPI(ticketPayload);
            }



            Payment payment = service.createPayment(orderPayload.getPrice(), orderPayload.getCurrency(), orderPayload.getMethod(),
                    orderPayload.getIntent(), orderPayload.getDescription(), "https://hanuairline4c.azurewebsites.net/payment/" + CANCEL_URL + "/" + order.getOrderId(),
                    "https://hanuairline4c.azurewebsites.net/payment/" + SUCCESS_URL + "/" + order.getOrderId());
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {

                    System.out.println("test redirect link :" + "redirect:" + link.getHref());
                    return "redirect:" + link.getHref();

                }
            }

        } catch (PayPalRESTException e) {
            // create Order over here but not saved to database
            // OrderPayload orderPayload = new
            //this.orderPayload=OrderPayLoad
           return (e.getMessage());
        }
        return "redirect:/";
    }

    // if paypal return falied
    @GetMapping(value = CANCEL_URL+"/{id}")
    public String cancelPay() {
        return
                "cancel";
    }

    // if succcess
    @GetMapping(SUCCESS_URL+"/{id}")
    public ResponseEntity<?> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,@PathVariable("id") Long orderId) {
            System.out.println("IN here");
//        return "redirect:/success.html";
        try {
            System.out.println("OrderId : "+orderId);
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
              List<TicketPayload> ticketPayloadList =  this.getDataFromTicketAPI();
              for(TicketPayload ticketPayload : ticketPayloadList){
                  if(ticketPayload.getOrder_id()==orderId){
                    Ticket ticket =  ticketService.createTicket(ticketPayload);
                      System.out.println("created ticket : " +ticket.toString());
                  }

              }
                URI userWeb = new URI("https://hanu-airline-app.web.app/");
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setLocation(userWeb);
                return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
            }
        } catch (PayPalRESTException e) {
            System.out.println("here");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidInputValueException | MessagingException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

   private Order postDataToOrderAPI( OrderPayload orderPayload ){
//        Order order = new Order(7L,1000000,"VND","paypal","test4","des4");
       List<Order> orderList = this.getDataFromOrderAPI();
       Long newId;
       if(orderList.isEmpty()){
           newId = 0L;
       }
       else{
           newId =orderList.get(orderList.size()-1).getOrderId()+1;
       }
       Order orderToAPI = new Order(newId,orderPayload.getPrice(),orderPayload.getCurrency(),orderPayload.getMethod(),orderPayload.getIntent(),orderPayload.getDescription());



       Gson json = new Gson();
        String toJson = json.toJson(orderToAPI);
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
       return orderToAPI;


   }

   private void postDataToTicketAPI( TicketPayload ticketPayload){
//        TicketPayload ticketPayload = new TicketPayload(3L,2L,"D1-3",CHILDREN,1L);
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




