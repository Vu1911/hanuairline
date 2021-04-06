package com.se2.hanuairline.service;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.exception.NoResultException;
import com.se2.hanuairline.exception.ResourceNotFoundException;
import com.se2.hanuairline.model.*;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
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
import com.se2.hanuairline.service.aircraft.AircraftSeatService;
import com.se2.hanuairline.service.airport.AirportService;
import com.se2.hanuairline.service.airport.AirwayService;
import com.se2.hanuairline.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private AircraftSeatService aircraftSeatService;

    @Autowired
    private TicketService ticketService;
    public Page<Flight> getAll(int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);
        return flightRepository.findAll(pagingSort);
    }

    public Flight getById(Long id) {
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

        // check duplication
        Optional<Flight> check = flightRepository.findByArrivalTimeAndDepartureTimeAndArrivalGate_IdAndDepartureGate_Id(request.getArrival_time(), request.getDeparture_time(), request.getArrival_gate_id(), request.getDeparture_gate_id());
        if(check.isPresent()){
            return null;
        }

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

    public List<Flight> searchOneWayFlights(SearchPayload searchPayload) throws InvalidInputValueException, NoResultException {
            // one way flight
        System.out.println("In Flight service");
        // filter bằng location
        List<Flight>  filteredByLocationFlights = this.filterByInputAndOutputLocation(searchPayload.getDepartureAirportOrCity(),searchPayload.getArrivalAirportOrCity());

       // filter by time
         List<Flight> filteredByTimeFlights = filterByTime(filteredByLocationFlights,searchPayload);



        // flight : filter by travelclassId và number of traveler  // đây sau này là hàm lấy được các ticket thỏa mãn (hạng bay + flight) này

        // bước 1 : với mỗi flight - > tìm được các aircrafts -> tìm được aircraft id
        // bước 2 : với mỗi aircraft_id và travelClassId -> tìm được các ghế máy bay AircraftSeat_Ids[]
        // Bước 3 : với mỗi ghế máy bay (biết đc máy bay,hạng bay) + chuyến bay -> tìm được các ticket cho chuyến bay đó Ticket[]
        // Bước 4 : Với mỗi ticket(của máy bay và hạng bay đó)-> tìm được số ticket chưa có user
        // Bước 5 : Nếu thỏa mãn < = số vé còn lại( chưa có user_id) -> flight thỏa mãn
        // bước 6 : tìm được các flight thỏa mãn - > trả về flight



         // filter
        return filteredByTimeFlights;
    }

    // filter by time oke
    // buoc 1 : loop qua cac Flight
    // buoc 2: voi moi flight lay ra departure date va arrival date (instant)
    // buoc 3 : format instant ve date
    //buoc 4 : formte date ve String
    // buoc 4 : compare search String va flight String

    private List<Flight> filterByTime(List<Flight> filteredByLoactionFlights,SearchPayload searchPayload) throws  NoResultException {
        List<Flight> filteredByTimeFlights = new ArrayList<Flight>();

        String searchDepartureDate = searchPayload.getDepartureTime().toString();
        String  searchArrivalDate =  searchPayload.getArrivalTime().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Flight flight : filteredByLoactionFlights) {
            // Xu ly date của flight
            Date flightDepartureDate = Date.from(flight.getDepartureTime());
            Date flightArrivalDate = Date.from(flight.getArrivalTime());
            String flightDepartureDateString = simpleDateFormat.format(flightDepartureDate);
            String flightArrivalDateString = simpleDateFormat.format(flightArrivalDate);
            System.out.println("biến flightDepartureDateString sau khi được convert : "+flightDepartureDateString);
            System.out.println("biến flightArrivalDateString sau khi được convert : "+flightArrivalDateString);

            if (flightDepartureDateString.equals(searchDepartureDate) && flightArrivalDateString.equals(searchArrivalDate)) {
                filteredByTimeFlights.add(flight);
            }
        }

        if(filteredByTimeFlights.isEmpty()){
            throw new NoResultException("Không có chuyến bay nào từ :"+searchPayload.getDepartureTime()+" đến :"+searchPayload.getArrivalTime());
        }

        return filteredByTimeFlights;

    }

    private  List<Flight>  filterByInputAndOutputLocation( String inputDeparturePlace,String inputArrivalPlace) throws InvalidInputValueException, NoResultException {
    System.out.println("In filter");

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
        System.out.println("departure la airport ?" +departurePlaceIsAirport+"arrival is a airport"+arrivalPlaceIsAirport);


        // nó cho mình 2 tên thành phố hoặc 2 tên airport
        // case 1 : 2 tên airport oke

        // bước 2 : kiểm tra có airway từ airport này đến airport kia không -> lấy được airway_id
        // bước 3 : query Flight table để trả về những chuyến bay có airway_id như này
        if(departurePlaceIsAirport&&arrivalPlaceIsAirport){
            System.out.println("Both are airport");
            Airport departureAirport = airportService.findAirportByName(inputDeparturePlace);
            Airport arrivalAirport = airportService.findAirportByName(inputArrivalPlace);

            Airway airway =   airwayService.findByArrivalAirportIdAndDepartureAirportId(arrivalAirport.getId(),departureAirport.getId());
            if(airway==null){
                throw new NoResultException("Does not exist airway from : 1stcase"+" to"+arrivalAirport.getName());
            }
            List<Flight> flights = flightRepository.findFlightByAirway_Id(airway.getId());
            flightList.add(flights);

        }
        // case 2 :tên 2 thành phố oke
        // bước 1 : kiểm tra có airport nào thuộc 2 thành phố đó không -> trả về được thanhpho1[airports]+thanhpho2[airports]
        // bước 2 : kiểm tra giữa các airport có tồn tại airway nào không -> nhận được 1 list airway
        // bước 3 : query Flight table để trả về những chuyến bay có airway_id như này
        else if((!departurePlaceIsAirport)&&(!arrivalPlaceIsAirport)){
            System.out.println("departure is city , arrival is city");
            List<Airway> airwayList = new ArrayList<Airway>();
            List<Airport> departureAirportList = airportService.getAirportsByCityName(inputDeparturePlace);
            List<Airport> arrivalAirportList = airportService.getAirportsByCityName(inputArrivalPlace);
            if(departureAirportList.isEmpty()||arrivalAirportList.isEmpty()){
                throw new NoResultException("Does not exist airway from 2ndcase"+ "+inputArrivalPlace");
            }
            for(Airport departureAirport : departureAirportList){
                    System.out.println(departureAirport);
                for(Airport arrivalAirport : arrivalAirportList){
                    System.out.println(arrivalAirport);
                    Airway airway =    airwayService.findByArrivalAirportIdAndDepartureAirportId(arrivalAirport.getId(),departureAirport.getId());
                    if(!airway.equals(null)){
                        airwayList.add(airway);
                    }
                }

            }
            System.out.println("airway list is empty ?"+airwayList.isEmpty());

            for(Airway airway : airwayList){
                System.out.println("Airway_id :" +airway.getId());
                List<Flight> flight=    flightRepository.findFlightByAirway_Id(airway.getId());
               System.out.println("No flight for the airway id ? "+flight.isEmpty());
               for(Flight eachFlight: flight){
                   System.out.println("Each flight :"+ eachFlight);
               }
                flightList.add(flight);
            }


        }
        //!!! với những flight có airway_Id như nhau cần có departure && arrival time khác nhau

        // case 3 : departure place là thành phố arrival place là sân bay OK
        // bước 1 : kiểm tra có airport ở thành phố đó không -> nhận được List<Airport>
        // bước 2 : kiểm tra có airway từ Các airport ở thành phố đó đến airport này không-> List<Airway>
        // bước 3 : kiểm tra các chuyến bay có airway_id như thế

        else if((!departurePlaceIsAirport)&&arrivalPlaceIsAirport){
            System.out.println("departure is city arrival is airport");
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

        // case 4 : departure place là airport và arrival place là thành phố OK
        else if(departurePlaceIsAirport&&!arrivalPlaceIsAirport){
    System.out.println("departure is airport arrival is city");
            List<Airway> airwayList = new ArrayList<Airway>();

            List<Airport> arrivalAirportList=  airportService.getAirportsByCityName(inputArrivalPlace);

            Airport departureAirport = airportService.findAirportByName(inputDeparturePlace);
            for(Airport airport : arrivalAirportList){
                Airway airway=   airwayService.findByArrivalAirportIdAndDepartureAirportId(airport.getId(),departureAirport.getId());
                airwayList.add(airway);
            }

            for(Airway airway : airwayList){
                List<Flight> flights=    flightRepository.findFlightByAirway_Id(airway.getId());
                flightList.add(flights);

            }


        }


        List<Flight> result  = new ArrayList<Flight>();
        for(List<Flight> flights : flightList){
            if(flights.isEmpty()) continue;

            for(Flight flight : flights){
                result.add(flight);
            }

        }

        return result;

    }

    public int checkRemainSlot(Long flightId) throws NoResultException {
       Optional<Flight> flightToCheck = flightRepository.findById(flightId);
       if(!flightToCheck.isPresent()){
           throw new NoResultException("Không có chuyến bay để kiểm tra remain slot với flight_id "+flightId);
       }
       Flight flight = flightToCheck.get();
       return flight.getRemainSlot();
    }






}
