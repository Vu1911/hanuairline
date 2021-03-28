package com.se2.hanuairline.service;



import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.PriceByClass;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.TravelClass;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.payload.PriceByClassPayload;
import com.se2.hanuairline.repository.PriceByClassRepository;
import com.se2.hanuairline.repository.aircraft.AircraftRepository;
import com.se2.hanuairline.repository.aircraft.TravelClassRepository;
import com.se2.hanuairline.repository.airport.AirwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PriceByClassService {
    @Autowired
    private AirwayRepository airwayRepository;
    @Autowired
    private PriceByClassRepository priceByClassRepository;

    @Autowired
    private TravelClassRepository travelClassRepository;

    // get all  finished
    public List<PriceByClass> getAllPriceByClass() {
        List<PriceByClass> getList =  priceByClassRepository.findAll();

        return getList;
    }
    // get one by travelclass id and airway id -> finished
    public PriceByClass getOnePriceByClass(Long id) throws InvalidInputValueException {
        Optional<PriceByClass> priceByClassData = priceByClassRepository.findById(id);
        if(priceByClassData.isPresent()){
            return priceByClassData.get();
        }
        else {
            throw new InvalidInputValueException("Not exist record with id : "+id);
        }
    }

    // update one
    public PriceByClass updateARecord(Long id,PriceByClassPayload priceByClassPayload) throws InvalidInputValueException {
//        PriceByClass priceByClass = priceByClassRepository.getOne(priceByClassDTO.getId());

        Optional<TravelClass> travelClass=travelClassRepository.findById(priceByClassPayload.getTravelclass_id());
        Optional<Airway> airway=airwayRepository.findById(priceByClassPayload.getTravelclass_id());
        // check id valid
        if(!checkExisted(id)){
            throw new InvalidInputValueException("id does not exist to update: "+id);
        }
        // check row
        if (checkExisted(priceByClassPayload.getTravelclass_id(),priceByClassPayload.getAirway_id())){
              throw new InvalidInputValueException("A record with these data already exist ");

         }
        // check update data
         if(!travelClass.isPresent()){
             throw new InvalidInputValueException("Invalid update travelclass_id"+priceByClassPayload.getTravelclass_id());
         }

        if(!airway.isPresent()){
            throw new InvalidInputValueException("Invalid update airway_id"+priceByClassPayload.getAirway_id());
        }
        Optional<PriceByClass> priceByClassData = priceByClassRepository.findById(id);
        PriceByClass priceByClass = priceByClassData.get();
        priceByClass.setTravelClass(travelClass.get());
        priceByClass.setAirway(airway.get());
        PriceByClass result=   priceByClassRepository.save(priceByClass);

        return result;

    }
    // chưa check API
    public List<PriceByClass> updateManyRecords(List<PriceByClassPayload> priceByClassPayloads) throws InvalidInputValueException {
  List<PriceByClass> updatedRecords = new ArrayList<>();
        String fails = "";
        for(PriceByClassPayload item : priceByClassPayloads ){
        try {

          PriceByClass priceByClass =   updateARecord(item.getId(), item);
          updatedRecords.add(priceByClass);
        }catch(Exception e){
            fails.concat(" "+e.getMessage());
            continue;
        }

        }
        if(!fails.equals("")){
            throw new InvalidInputValueException(fails);
        }
        return updatedRecords;
    }
    // add one

    public PriceByClass insertNewRecord(PriceByClassPayload priceByClassPayload) throws InvalidInputValueException {
        if(this.checkExisted(priceByClassPayload.getTravelclass_id(),priceByClassPayload.getAirway_id())) throw new InvalidInputValueException("duplicate row data");
        Long travelClassId = priceByClassPayload.getTravelclass_id();
        Long airwayId = priceByClassPayload.getAirway_id();
        // check if existed before
        Optional<Airway> airway = airwayRepository.findById(airwayId);
       Optional<TravelClass> travelClass = travelClassRepository.findById(travelClassId);
       if((!airway.isPresent())){
           throw new InvalidInputValueException("Can't find airway with id :" +airwayId);
       }
       if(!travelClass.isPresent()){
           throw new InvalidInputValueException("Can't find travelClass with id :" +travelClassId);

       }
        PriceByClass priceByClass = new PriceByClass();
        priceByClass.setAirway(airway.get());
        priceByClass.setTravelClass(travelClass.get());
        priceByClass.setPrice(priceByClassPayload.getPrice());
        PriceByClass updatedPriceByClass= priceByClassRepository.save(priceByClass);



        return updatedPriceByClass;

    }

    // delete many
    public List<PriceByClass> deleteManyRecords(Long[]ids) throws InvalidInputValueException {
        List<PriceByClass> deletedRecords = new ArrayList<>();
        String fails="can not delete records with ids: ";
    for(Long id : ids){
     Optional<PriceByClass> priceByClass= priceByClassRepository.findById(id);
        if(priceByClass.isPresent()){
            deletedRecords.add(priceByClass.get());
            priceByClassRepository.deleteById(id);
        }else{
            fails.concat(" "+id);
        }
    }
    if(!fails.equals("")){
        throw new InvalidInputValueException(fails);
    }


    return deletedRecords;

    }
    //
    public PriceByClass deleteOneRecord(Long id) throws InvalidInputValueException {
        PriceByClass priceByClass;
        Optional<PriceByClass>priceByClassData=priceByClassRepository.findById(id);
        if(priceByClassData.isPresent()){
            priceByClass = priceByClassData.get();
            priceByClassRepository.deleteById(id);
        }
        else{
            throw new InvalidInputValueException("Not exist a record with this id :"+id);
        }
        return priceByClass;
    }
     // exist a row
    private boolean checkExisted(Long travelClassId,Long airwayId){
     Optional<PriceByClass> duplicatedRecord= priceByClassRepository.findByTravelClass_IdAndAirway_Id(travelClassId, airwayId);
      return (duplicatedRecord.isPresent()) ;


    }

    private boolean checkExisted(Long id){
       Optional<PriceByClass> priceByClass = priceByClassRepository.findById(id);
        return priceByClass.isPresent();
    }



}

