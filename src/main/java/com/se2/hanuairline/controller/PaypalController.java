package com.se2.hanuairline.controller;

import com.se2.hanuairline.Order;
import com.se2.hanuairline.PaypalService;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.payload.OrderPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaypalController {

    //temp
//    private static OrderPayload orderPayload;

    @Autowired
    PaypalService service;


    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @GetMapping("/")
    public String home() {
        System.out.println("In home");
        return "home";
    }
    //@ModelAttribute("order")
    @PostMapping("/pay")
    public String payment(@RequestBody OrderPayload orderPayload) {
        try {

            // Order

            System.out.println(("In pay"));
            System.out.println("Order "+orderPayload.toString());
            Payment payment = service.createPayment(orderPayload.getPrice(), orderPayload.getCurrency(), orderPayload.getMethod(),
                    orderPayload.getIntent(), orderPayload.getDescription(), "http://localhost:8080/" + CANCEL_URL,
                    "http://localhost:8080/" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    // create Order over here but not saved to database
                    // OrderPayload orderPayload = new
                    //this.orderPayload=OrderPayLoad
                    System.out.println("test redirect link :"+"redirect:"+link.getHref());
                    return "redirect:"+link.getHref();
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
    @GetMapping(value = SUCCESS_URL)
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

}
