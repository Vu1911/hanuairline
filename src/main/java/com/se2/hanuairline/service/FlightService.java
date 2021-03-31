package com.se2.hanuairline.service;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.exception.NoResultException;
import com.se2.hanuairline.exception.ResourceNotFoundException;
import com.se2.hanuairline.model.DiscountEvent;
import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.FlightDirection;
import com.se2.hanuairline.model.FlightStatus;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.airport.Airport;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.model.airport.Gate;
import com.se2.hanuairline.payload.FlightPayload;
import com.se2.hanuairline.payload.SearchPayload;
import com.se2.hanuairline.repository.DiscountEventRepository;
import com.se2.hanuairline.repository.FlightRepository;
import com.se2.hanuairline.repository.aircraft.AircraftRepository;
import com.se2.hanuairline.repository.airport.AirwayRepository;
import com.se2.hanuairline.repository.airport.GateRepository;
import com.se2.hanuairline.service.airport.AirportService;
import com.se2.hanuairline.service.airport.AirwayService;
import com.se2.hanuairline.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private DiscountEventRepository discountEventRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private AirwayRepository airwayRepository;

    @Autowired
    private GateRepository gateRepository;
    @Autowired
    private AirportService airportService;
    @Autowired
    private AirwayService airwayService;
    public Page<Flight> getAll(int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);
        return flightRepository.findAll(pagingSort);
    }

    public Flight getById(Long id){
        Optional<Flight> flightData = flightRepository.findById(id);

        if(flightData.isPresent()){
            return flightData.get();
        } else {
            return null;
        }
    }

    public Flight createFlight(FlightPayload request) throws InvalidInputValueException {
        Optional<Aircraft> aircraftData = aircraftRepository.findById(request.getAircraft_id());
        Optional<Airway> airwayData = airwayRepository.findById(request.getAirway_id());
        Optional<Gate> arrival_gateData = gateRepository.findById(request.getArrival_gate_id());
        Optional<Gate> departure_gateData = gateRepository.findById(request.getDeparture_gate_id());

        Flight flight = new Flight();

        if (request.getDiscount_id() != null){
            Optional<DiscountEvent> discountEventData = discountEventRepository.findById(request.getDiscount_id());
            if(!discountEventData.isPresent()){
                throw new InvalidInputValueException("FlightController: discount event not found");
            }

            flight.getDiscount().add(discountEventData.get());
        }

        if(!aircraftData.isPresent()){
            throw new InvalidInputValueException("FlightController: aircraft not found");
        }

        if(!airwayData.isPresent()){
            throw new InvalidInputValueException("FlightController: airway not found");
        }

        if(!arrival_gateData.isPresent()){
            throw new InvalidInputValueException("FlightController: arrival gate not found");
        }

        if(!departure_gateData.isPresent()){
            throw new InvalidInputValueException("FlightController: departure gate not found");
        }

        if(!aircraftData.isPresent()){
            throw new InvalidInputValueException("FlightController: aircraft not found");
        }


        flight.setAircraft(aircraftData.get());
        flight.setAirway(airwayData.get());
        flight.setArrivalGate(arrival_gateData.get());
        flight.setArrivalTime(request.getArrival_time());
        flight.setDepartureGate(departure_gateData.get());
        flight.setDepartureTime(request.getDeparture_time());
        flight.setStatus(FlightStatus.valueOf(request.getStatus()));

        Flight _flight = flightRepository.save(flight);

        return _flight;
    }

//    public List<Flight> searchOneWayFlights(SearchPayload searchPayload) throws InvalidInputValueException, NoResultException {
//            // one way flight
//
//        // filter bằng location
////        String inputDeparturePlace = searchPayload.getDepartureAirportOrCity();
////        String inputArrivalPlace =  searchPayload.getArrivalAirportOrCity();
//
//        List<List<Flight>> filteredByLoactionFlights = this.filterByInputAndOutputLocation(searchPayload.getDepartureAirportOrCity(),searchPayload.getArrivalAirportOrCity());
//
//        Date arrivalDate = searchPayload.getArrivalTime();
//        Date departureDate = searchPayload.getDepartureTime();
//        int numberOfTravelers = searchPayload.getNumberOfTraveler();
//        Long travelClassId = searchPayload.getTravelClassId();
//    }


    private List<List<Flight>> filterByInputAndOutputLocation( String inputDeparturePlace,String inputArrivalPlace) throws InvalidInputValueException, NoResultException {


//
        boolean departurePlaceIsAirport = true;
        boolean arrivalPlaceIsAirport = true;
        List<List<Flight>> flightList=new ArrayList<List<Flight>>();
        if(!airportService.checkExistedByAirportName(inputDeparturePlace)){
            departurePlaceIsAirport =false;
            // không có thành phố nào như vậy mà có chứa bất kì airport nào
            if(!airportService.checkExistedAirportsByCityName(inputDeparturePlace)){
                throw new InvalidInputValueException(("Điền sai thông tin nơi đi: "+inputDeparturePlace));
            }
        }
        if(!airportService.checkExistedByAirportName(inputArrivalPlace)){
            arrivalPlaceIsAirport = false;
            // không có thành phố nào như vậy mà có chứa bất kì airport nào
            if(!airportService.checkExistedAirportsByCityName(inputArrivalPlace)){
                throw new InvalidInputValueException(("Điền sai thông tin nơi đến: "+inputArrivalPlace));
            }
        }


        // nó cho mình 2 tên thành phố hoặc 2 tên airport
        // case 1 : 2 tên airport

        // bước 2 : kiểm tra có airway từ airport này đến airport kia không -> lấy được airway_id
        // bước 3 : query Flight table để trả về những chuyến bay có airway_id như này
        if(departurePlaceIsAirport&&arrivalPlaceIsAirport){
            Airport departureAirport = airportService.findAirportByName(inputDeparturePlace);
            Airport arrivalAirport = airportService.findAirportByName(inputArrivalPlace);
            Airway airway =   airwayService.findByArrivalAirportIdAndDepartureAirportId(arrivalAirport.getId(),departureAirport.getId());
            if(airway==null){
                throw new NoResultException("Does not exist airway from : "+departureAirport.getName()+" to"+arrivalAirport.getName());
            }
            List<Flight> flights = flightRepository.findFlightByAirway_Id(airway.getId());
            flightList.add(flights);

        }
        // case 2 :tên 2 thành phố
        // bước 1 : kiểm tra có airport nào thuộc 2 thành phố đó không -> trả về được thanhpho1[airports]+thanhpho2[airports]
        // bước 2 : kiểm tra giữa các airport có tồn tại airway nào không -> nhận được 1 list airway
        // bước 3 : query Flight table để trả về những chuyến bay có airway_id như này
        else if((!departurePlaceIsAirport)&&!arrivalPlaceIsAirport){
            List<Airway> airwayList = new ArrayList<Airway>();
            List<Airport> departureAirportList = airportService.getAirportsByCityName(inputDeparturePlace);
            List<Airport> arrivalAirportList = airportService.getAirportsByCityName(inputArrivalPlace);
            if(departureAirportList.isEmpty()||arrivalAirportList.isEmpty()){
                throw new NoResultException("Does not exist airway from "+inputDeparturePlace+" to "+inputArrivalPlace);
            }
            for(Airport departureAirport : departureAirportList){

                for(Airport arrivalAirport : arrivalAirportList){
                    Airway airway =    airwayService.findByArrivalAirportIdAndDepartureAirportId(arrivalAirport.getId(),departureAirport.getId());
                    airwayList.add(airway);
                }

            }
            List<List<Flight>> flights = new ArrayList<List<Flight>>();
            for(Airway airway : airwayList){
                List<Flight> flight=    flightRepository.findFlightByAirway_Id(airway.getId());
                flights.add(flight);
            }


        }
        //!!! với những flight có airway_Id như nhau cần có departure && arrival time khác nhau

        // case 3 : departure place là thành phố arrival place là sân bay
        // bước 1 : kiểm tra có airport ở thành phố đó không -> nhận được List<Airport>
        // bước 2 : kiểm tra có airway từ Các airport ở thành phố đó đến airport này không-> List<Airway>
        // bước 3 : kiểm tra các chuyến bay có airway_id như thế

        else if((!departurePlaceIsAirport)&&arrivalPlaceIsAirport){
            List<Airway> airwayList = new ArrayList<Airway>();
            // tìm được các airport của  thành phố
            List<Airport> departureAirportList=  airportService.getAirportsByCityName(inputDeparturePlace);
            // tìm  airport
            Airport arrivalAirport = airportService.findAirportByName(inputArrivalPlace);
            // với arrival airport này thì có các đường bay nào từ các airport của thành phố
            for(Airport airport : departureAirportList){
                Airway airway=   airwayService.findByArrivalAirportIdAndDepartureAirportId(arrivalAirport.getId(),airport.getId());
                airwayList.add(airway);
            }

            for(Airway airway : airwayList){
                List<Flight> flights=    flightRepository.findFlightByAirway_Id(airway.getId());
                flightList.add(flights);

            }

        }

        // case 4 : departure place là airport và arrival place là thành phố
        else if(departurePlaceIsAirport&&!arrivalPlaceIsAirport){

            List<Airway> airwayList = new ArrayList<Airway>();

            List<Airport> arrivalAirportList=  airportService.getAirportsByCityName(inputArrivalPlace);

            Airport departureAirport = airportService.findAirportByName(inputArrivalPlace);
            for(Airport airport : arrivalAirportList){
                Airway airway=   airwayService.findByArrivalAirportIdAndDepartureAirportId(airport.getId(),departureAirport.getId());
                airwayList.add(airway);
            }

            for(Airway airway : airwayList){
                List<Flight> flights=    flightRepository.findFlightByAirway_Id(airway.getId());
                flightList.add(flights);

            }


        }




        return flightList;

    }




}
