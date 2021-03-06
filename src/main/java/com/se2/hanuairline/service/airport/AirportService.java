package com.se2.hanuairline.service.airport;

import com.se2.hanuairline.model.airport.Airport;
import com.se2.hanuairline.model.airport.AirportStatus;
import com.se2.hanuairline.payload.airport.AirportPayload;
import com.se2.hanuairline.repository.airport.AirportRepository;
import com.se2.hanuairline.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    public Page<Airport> findAll (Long id, String name, String country, String city, String status, int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);

        if(id != null){
            return airportRepository.findById(id, pagingSort);
        }

        if(name.equals("_") && country.equals("_") && city.equals("_") && status.equals("_")){
            return airportRepository.findAll(pagingSort);
        }

        return airportRepository.findByNameContainingAndCountryContainingAndCityContainingAndStatus(name, country, city, status, pagingSort);
    }

    public Airport getById(long id){
        Optional<Airport> airport = airportRepository.findById(id);

        if (airport.isPresent()) {
            return airport.get();
        } else {
            return null;
        }
    }

    public Airport createAirport(AirportPayload request){
        Airport airport = new Airport();
        airport.setName(request.getName());
        airport.setCity(request.getCity());
        airport.setCountry(request.getCountry());
        airport.setCapacity(request.getCapacity());
        airport.setStatus(AirportStatus.OPENED);
        Airport _airport = airportRepository.save(airport);
        return _airport;
    }

    public Airport updateAirport(long id, AirportPayload request){
        Optional<Airport> airportData = airportRepository.findById(id);

        if (airportData.isPresent()) {
                Airport airport = airportData.get();

                airport.setName(request.getName());
                airport.setCity(request.getCity());
                airport.setCountry(request.getCountry());

                Airport _airport = airportRepository.save(airport);

                return _airport;
        } else {
            return null;
        }
    }

    public boolean deleteAirport(long id){
        Optional<Airport> airport = airportRepository.findById(id);

        if(!airport.isPresent()){
            return false;
        }

        if(airport.get().getAirway1().isEmpty()){
            airportRepository.delete(airport.get());
            return true;
        } else {
            airport.get().setStatus(AirportStatus.CLOSED);
            return true;
        }
    }

    public List<Airport> getAirportsByCityName(String cityName){
        System.out.println("airport of "+cityName);
        List<Airport> cityAirports = airportRepository.findAirportByCity(cityName);
        System.out.println("City airport empty ? "+ cityAirports.isEmpty());
        if(cityAirports.isEmpty()){
            return null;
        }
        return cityAirports;

    }

    public Airport findAirportByName(String airportName){

        if(airportRepository.findAirportByName(airportName).isPresent()) {
            return airportRepository.findAirportByName(airportName).get();
        }
        return null;

    }



    public boolean checkExistedByAirportName(String airportName){
        System.out.println("In checkexiested by airport name"+airportName);
      Optional<Airport> airportList =  airportRepository.findAirportByName(airportName);

      return airportList.isPresent();
    }

    public boolean checkExistedAirportsByCityName(String cityName){
        System.out.println("In checkedAirport by CityName"+cityName);
         List<Airport> airportList=   airportRepository.findAirportByCity(cityName);
         System.out.println("Hi" +airportList);
         System.out.println("airport is empty ?" + airportList.isEmpty());
    return !airportList.isEmpty();
    }

}
