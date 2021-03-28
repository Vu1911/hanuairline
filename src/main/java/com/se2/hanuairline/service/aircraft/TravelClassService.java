package com.se2.hanuairline.service.aircraft;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.aircraft.TravelClass;
import com.se2.hanuairline.payload.aircraft.TravelClassPayload;
import com.se2.hanuairline.repository.aircraft.TravelClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelClassService {

    @Autowired
    private TravelClassRepository travelClassRepository;

    public List<TravelClass> getAllTravelClass(){
        return travelClassRepository.findAll();

    }



    public TravelClass getRecordById (Long id) throws InvalidInputValueException {

        Optional<TravelClass> travelClassData = travelClassRepository.findById(id);

        if(travelClassData.isPresent()){
            return travelClassData.get();
        }
        else{
            throw new InvalidInputValueException("Không tồn tại TravelClass với id : "+id);
        }
    }
    public TravelClass createNewRecord(TravelClassPayload travelClassPayload) throws InvalidInputValueException {
        if(checkExisted(travelClassPayload.getName(),travelClassPayload.getDescription())){
            throw new InvalidInputValueException("Invalid input to create :row data trùng lặp ");

        }

        TravelClass travelClass = new TravelClass();
        travelClass.setName(travelClassPayload.getName());
        travelClass.setDescription(travelClassPayload.getDescription());
//        System.out.println(travelClass.toString());

    TravelClass result =  travelClassRepository.save(travelClass);

        return result;

    }


    public TravelClass updateARecordById(Long id,TravelClassPayload travelClassPayload) throws InvalidInputValueException {

        if(!checkExisted(id)){
            throw new InvalidInputValueException("update id does not exist :  "+id);
        }
        if(checkExisted(travelClassPayload.getName(),travelClassPayload.getDescription())){
            throw new InvalidInputValueException("duplicate record data : "+"name : "+travelClassPayload.getName()+" description: "+travelClassPayload.getDescription());
        }

        TravelClass travelClass = this.getRecordById(id);
        travelClass.setName(travelClassPayload.getName());
        travelClass.setDescription(travelClassPayload.getDescription());
        TravelClass result= travelClassRepository.save(travelClass);
        return result;
    };

    public TravelClass deleteARecordById(Long id) throws InvalidInputValueException {
        if(!checkExisted(id)){
            throw new InvalidInputValueException("Not exist travel class with id :"+id);
        }
        TravelClass result= getRecordById(id);
        travelClassRepository.deleteById(id);
        return result;
    }

    private boolean checkExisted(String name, String description){

       System.out.println("here");
        Optional<TravelClass> data = travelClassRepository.findByNameAndDescription(name,description);
        if(data.isPresent()){
            System.out.println(data.get().toString());
            return true;
        }
            return false;
    }
    private boolean checkExisted(Long id){
        Optional<TravelClass> checkRecord= travelClassRepository.findById(id);
        if(checkRecord.isPresent()){
            return true;
        }
        return false;
    }







}
