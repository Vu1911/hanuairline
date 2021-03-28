package com.se2.hanuairline.controller;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.PriceByClass;
import com.se2.hanuairline.payload.PriceByClassPayload;
import com.se2.hanuairline.service.PriceByClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


    @RestController
    @RequestMapping("/priceByClass")
    public class PriceByClassController {
        // controller mapping request / gọi service , trả data về dạng json
        // mapping request
        @Autowired
        private PriceByClassService priceByClassService;


        // Read all finished -> checked API
        @GetMapping("/allRecords")
        public ResponseEntity<?> showAllPriceByClass(){
//            priceByClassService = new PriceByClassService();
            List<PriceByClass> result= priceByClassService.getAllPriceByClass();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        // get one base on id // checked API
        @GetMapping("/get-one/{id}")
        public ResponseEntity<?> showOnePriceByClass(@PathVariable("id")Long id){
            ResponseEntity<?> responseEntity;
            PriceByClass result = null;
            try {
                result = priceByClassService.getOnePriceByClass(id);
            responseEntity  = new ResponseEntity<>(result,HttpStatus.OK);
            } catch (InvalidInputValueException e) {
                responseEntity  = new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
            }
            return responseEntity;

        }
        // add One -> finished , checked API
        @PostMapping("/insert")
        public ResponseEntity<?> insertPriceByClass(@RequestBody PriceByClassPayload priceByClassPayload){

            PriceByClass result= null;
            ResponseEntity<?> responseEntity;
            try {
                result = priceByClassService.insertNewRecord(priceByClassPayload);
             // after insert success
                responseEntity = new ResponseEntity<>(result,HttpStatus.OK);
            } catch (InvalidInputValueException e) {
                // insert failed
                responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
            }

        return responseEntity;
        }

        // update one // finished // checked API
        @PutMapping("/update-one/{id}")
        public ResponseEntity<?> updateOneRecord(@RequestBody PriceByClassPayload priceByClassPayload, @PathVariable(name="id")Long id) { // phai gui content update len nua
            ResponseEntity<?> responseEntity ;

                try {
                    System.out.println("Check controller data : "+priceByClassPayload.getTravelclass_id()+" +"+priceByClassPayload.getAirway_id());
                    PriceByClass result= priceByClassService.updateARecord(id,priceByClassPayload);
                    responseEntity = new ResponseEntity<>(result,HttpStatus.OK);
            } catch (InvalidInputValueException e) {
                    responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
            }
            return responseEntity;

        }



        // update records with new data based on their ids // finished // checked API
        @PutMapping("/update-many")
        public ResponseEntity<?> updateManyRecords(@RequestBody List<PriceByClassPayload> priceByClassPayloads){
            // String --------------------------------
            ResponseEntity<?> responseEntity;
            List<PriceByClass> result ;
            try {
                result = priceByClassService.updateManyRecords(priceByClassPayloads);
                responseEntity = new ResponseEntity<>(result,HttpStatus.OK);
            } catch (InvalidInputValueException e) {
                responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
            }
            return responseEntity;
        }


        // finished // checked API
        @DeleteMapping("/delete-many")
        public ResponseEntity<?> deleteManyPriceByClass(@RequestBody Long[]ids){
            ResponseEntity<?> responseEntity;
            List<PriceByClass> result= null;
            try {
                System.out.println(Arrays.toString(ids));
                result = priceByClassService.deleteManyRecords(ids);
                responseEntity = new ResponseEntity<>(result,HttpStatus.OK);
            } catch (InvalidInputValueException e) {
                responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
            }
            return responseEntity;

        }
        // delete a record by id // finished // checked API
        @DeleteMapping("/delete-one/{id}")
        public ResponseEntity<?> deleteAPriceByClass(@PathVariable("id") Long id){
            ResponseEntity<?> responseEntity;
            PriceByClass priceByClass=null;
            try {
                 priceByClass = priceByClassService.deleteOneRecord(id);
                 responseEntity = new ResponseEntity<>(priceByClass,HttpStatus.OK);
            } catch (InvalidInputValueException e) {
                responseEntity = new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
            }
            return responseEntity;
        }

    }


