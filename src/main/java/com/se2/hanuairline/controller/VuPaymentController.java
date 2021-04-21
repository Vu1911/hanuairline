//package com.se2.hanuairline.controller;
//
//import com.paypal.api.payments.Links;
//import com.paypal.api.payments.Payment;
//import com.paypal.base.rest.PayPalRESTException;
//import com.se2.hanuairline.PaypalService;
//import com.se2.hanuairline.payload.OrderPayload;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/payme")
//public class VuPaymentController {
//
//    @Autowired
//    PaypalService service;
//
//    @PostMapping("/pay")
//    public String payment(@RequestBody OrderPayload orderPayload) {
//        try {
//
//            // Order
//
//            System.out.println(("In pay"));
//            System.out.println("Order "+orderPayload.toString());
//            Payment payment = service.createPayment(orderPayload.getPrice(), orderPayload.getCurrency(), orderPayload.getMethod(),
//                    orderPayload.getIntent(), orderPayload.getDescription(), "http://localhost:8080/",
//                    "http://localhost:8080/");
//            for(Links link:payment.getLinks()) {
//                if(link.getRel().equals("approval_url")) {
//                    // create Order over here but not saved to database
//                    // OrderPayload orderPayload = new
//                    //this.orderPayload=OrderPayLoad
//                    System.out.println("test redirect link :"+"redirect:"+link.getHref());
//                    return "redirect:"+link.getHref();
//                }
//            }
//
//        } catch (PayPalRESTException e) {
//
//            e.printStackTrace();
//        }
//        return "redirect:/";
//    }
//}
