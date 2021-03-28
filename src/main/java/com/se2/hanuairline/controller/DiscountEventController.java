package com.se2.hanuairline.controller;

import com.se2.hanuairline.model.DiscountEvent;
import com.se2.hanuairline.payload.DiscountEventPayload;
import com.se2.hanuairline.repository.DiscountEventRepository;
import com.se2.hanuairline.service.DiscountEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/discountEvent")
public class DiscountEventController {

    @Autowired
    private DiscountEventRepository discountEventRepository;

    @Autowired
    private DiscountEventService discountEventService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllDiscountEvent() {
        return new ResponseEntity<>(discountEventService.getAllDiscountEvent(), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getDiscountEventById(@PathVariable("id") long id) {
        DiscountEvent discountEvent = discountEventService.getById(id);

        if (discountEvent != null) {
            return new ResponseEntity<>(discountEvent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDiscountEvent(@RequestBody DiscountEventPayload request) {
        try {
            DiscountEvent _discountEvent = discountEventService.createDiscountEvent(request);
            return new ResponseEntity<>(_discountEvent, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> updateDiscountEvent(@PathVariable("id") long id, @RequestBody DiscountEventPayload request) {
        DiscountEvent discountEventData = discountEventService.updateDiscountEvent(id, request);

        if (discountEventData != null) {
                return new ResponseEntity<>(discountEventData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteDiscountEvent(@PathVariable("id") long id) {
        if(discountEventService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else  {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
