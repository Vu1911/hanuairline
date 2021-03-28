package com.se2.hanuairline.service;

import com.se2.hanuairline.model.DiscountEvent;
import com.se2.hanuairline.payload.DiscountEventPayload;
import com.se2.hanuairline.repository.DiscountEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountEventService {

    @Autowired
    private DiscountEventRepository discountEventRepository;

    public List<DiscountEvent> getAllDiscountEvent(){
        return discountEventRepository.findAll();
    }

    public DiscountEvent getById(Long id){
        Optional<DiscountEvent> discountEvent = discountEventRepository.findById(id);

        if (discountEvent.isPresent()) {
            return discountEvent.get();
        } else {
            return null;
        }
    }

    public DiscountEvent createDiscountEvent(DiscountEventPayload request){
        DiscountEvent discountEvent = new DiscountEvent();

        discountEvent.setDiscountRate(request.getDiscount_rate_percentage());

        DiscountEvent _discount = discountEventRepository.save(discountEvent);
        return _discount;
    }

    public DiscountEvent updateDiscountEvent(Long id, DiscountEventPayload request){
        Optional<DiscountEvent> discountEventData = discountEventRepository.findById(id);

        if (discountEventData.isPresent()) {
            try {
                DiscountEvent discountEvent = new DiscountEvent();

                discountEvent.setDiscountRate(request.getDiscount_rate_percentage());

                DiscountEvent _discount = discountEventRepository.save(discountEvent);

                return _discount;
            } catch (Exception e){
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean deleteById(Long id){
        try{
            discountEventRepository.deleteById(id);
            return true;
        } catch(Exception e){
            return false;
        }
    }
}
