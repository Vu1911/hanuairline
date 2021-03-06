package com.se2.hanuairline.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import com.se2.hanuairline.model.*;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.payload.output.AircraftSeatOutputPayload;
import com.se2.hanuairline.service.aircraft.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.exception.NoResultException;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftStatus;
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
import com.se2.hanuairline.service.aircraft.SeatsByClassService;
import com.se2.hanuairline.service.aircraft.TravelClassService;
import com.se2.hanuairline.service.airport.AirportService;
import com.se2.hanuairline.service.airport.AirwayService;
import com.se2.hanuairline.util.PaginationUtils;

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

    @Autowired
    private SeatsByClassService seatsByClassService;

    @Autowired
    private TravelClassService travelClassService;

    @Autowired
    private AircraftService aircraftService;

    @Autowired
    private DiscountEventService discountEventService;

    // flight validated
    // lay cac seats
    // check ticket -> seats status
    // dua vao travelclass -> them vao cac list
    // out put m??nh c???n -> c??c gh??? ng???i ???????c ph??n chia th??nh booked v?? not booked -> 2 list
    public List<List<AircraftSeatOutputPayload>> getSeatsAndStatus(Long flightId){
        List<List<AircraftSeatOutputPayload>> outputPayloads = new ArrayList<List<AircraftSeatOutputPayload>>();
        List<AircraftSeatOutputPayload> bookedSeats = new ArrayList<>();
        List<AircraftSeatOutputPayload> notBookedSeats = new ArrayList<>();
        outputPayloads.add(bookedSeats);
        outputPayloads.add(notBookedSeats);
        // ch???c ch???n c?? flight v???i flightId n??y
        Flight flight = flightRepository.findFlightById(flightId);
        Aircraft aircraft = flight.getAircraft();
        // v?? c?? aircraft - > ph???i c?? aircraft seat
        List<AircraftSeat> seats = aircraftSeatService.getByAircrafId(aircraft.getId());
      List<Ticket> ticketList = ticketService.getByFlightId(flightId);
      // if do not exist any booked Seat -> all seat available
      if(ticketList.isEmpty()){
          AircraftSeatOutputPayload temp;
          for(AircraftSeat aircraftSeat : seats){
              temp = new AircraftSeatOutputPayload(aircraftSeat.getId(),aircraftSeat.getTravelClass().getId(),AircraftSeatStatus.AVAILABLE);
              notBookedSeats.add(temp);
          }
          return outputPayloads;

      }
//
      List<String> bookedSeatIdList = new ArrayList<String>();
      for(Ticket ticket : ticketList){
          bookedSeatIdList.add(ticket.getAircraftSeat().getId());
      }
      System.out.println("bookedSeatIdList"+bookedSeatIdList);
      // neu trung id voi bookedIdList -> tao mot aircraftSeatOutputPayload ->
      for(AircraftSeat aircraftSeat : seats){
          boolean check = false;
          AircraftSeatOutputPayload aircraftSeatOutputPayload;
          for(String bookedSeatId : bookedSeatIdList ){
              if(aircraftSeat.getId().equals(bookedSeatId)){
                 aircraftSeatOutputPayload = new AircraftSeatOutputPayload(aircraftSeat.getId(),aircraftSeat.getTravelClass().getId(), AircraftSeatStatus.BOOKED);
                 bookedSeats.add(aircraftSeatOutputPayload);
                 notBookedSeats.add(aircraftSeatOutputPayload);
                 check=true;
                 break;
              }
          }
          // if not booked
          if(!check){
              aircraftSeatOutputPayload = new AircraftSeatOutputPayload(aircraftSeat.getId(),aircraftSeat.getTravelClass().getId(),AircraftSeatStatus.AVAILABLE);
              notBookedSeats.add(aircraftSeatOutputPayload);
          }

      }
      return outputPayloads;


    }
    public Page<Flight> getAll(boolean saleFlightOnly, int page, int size, String[] sort){
        Pageable pagingSort = PaginationUtils.pagingSort(page, size, sort);
        if (!saleFlightOnly){
            return flightRepository.findAll(pagingSort);
        } else {
            return flightRepository.findAllByDiscountIsNotNull(pagingSort);
        }


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
            throw new InvalidInputValueException("Flight v???i departure_time :"+request.getDeparture_time()+" arrival time: "+request.getArrival_time()+" departure_gate_id "+request.getDeparture_gate_id()+" arrival_gate_id "+request.getArrival_gate_id());
        }
        // check aircraftId
        if(aircraftService.getAircraftById(request.getAircraft_id())==null){
            throw new InvalidInputValueException("Aircraft_id not exist :"+request.getAircraft_id());

        };

        Flight flight = new Flight();

        if (request.getDiscount_id() != null){
            Optional<DiscountEvent> discountEventData = discountEventRepository.findById(request.getDiscount_id());
            if(!discountEventData.isPresent()){
                throw new InvalidInputValueException("FlightController: discount event not found");
            }
            System.out.println("DISCOUNT EVENT IN CHECK: "+discountEventData.get());
            flight.setDiscount(new HashSet<DiscountEvent>());
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


        Aircraft aircraft = aircraftData.get();
        Airway airway = airwayData.get();
        Gate arrivalGate = arrival_gateData.get();
        Gate departureGate = departure_gateData.get();

        if(arrivalGate.getAirport().getId() != airway.getArrivalAirport().getId()){
            throw new InvalidInputValueException("FlightController: Wrong arrival gate");
        }

        if(departureGate.getAirport().getId() != airway.getDepartureAirport().getId()){
            throw new InvalidInputValueException("FlightController: Wrong departure gate");
        }

        if(!checkPriceAvailability(airway, aircraft)){
            throw new InvalidInputValueException("FlightController: Airway is not been fully setted in price");
        }

        if(!checkAircraftAvailability(aircraft, request.getDeparture_time(), airway.getDepartureAirport())){
            throw new InvalidInputValueException("FlightController: Aircraft not available");
        }



        flight.setAircraft(aircraft);
        flight.setAirway(airway);
        flight.setArrivalGate(arrival_gateData.get());
        flight.setArrivalTime(request.getArrival_time());
        flight.setDepartureGate(departure_gateData.get());
        flight.setDepartureTime(request.getDeparture_time());
        flight.setStatus(FlightStatus.valueOf(request.getStatus()));

//        System.out.println("before");
//        flight.setDiscount(new HashSet<DiscountEvent>()
//        );
//        System.out.println("flight discount set : "+flight.getDiscount());
//        flight.getDiscount().add(discountEvent);
//
     Flight _flight = flightRepository.save(flight);

        return _flight;
    }

    public boolean checkAircraftAvailability(Aircraft aircraft, Instant requestDepartureTime, Airport requestedDepartureAirport){
        if (aircraft.getStatus().equals(AircraftStatus.DEACTIVATED)){
            return false;
        }

        // Get the lastest flight of the given aircraft
        Flight lastestFlight = flightRepository.findDistinctFirstByAircraft(aircraft, Sort.by(Sort.Direction.DESC, "arrivalTime"));
        System.out.println("Getting latest flight : "+lastestFlight);
        if(lastestFlight == null){
            return true;
        }

        Instant lastestArrivalTime = lastestFlight.getArrivalTime();
        System.out.println("Latest arrival time" +lastestArrivalTime);
        Airport lastestArrivalAirport = lastestFlight.getAirway().getArrivalAirport();
        System.out.println("latest arriaval airport"+lastestArrivalAirport);
        System.out.println("Lastest arrival time less than request departure time ?:" +lastestArrivalTime.compareTo(requestDepartureTime));
        if (lastestArrivalTime.compareTo(requestDepartureTime) < 0){ // ??i???u ki???n chu???n
            System.out.println(lastestArrivalAirport.getId().equals(requestedDepartureAirport.getId()));
            System.out.println("latest arrival airport id :"+lastestArrivalAirport.getId());
            System.out.println("request departure airport id :" +requestedDepartureAirport.getId());
            if (lastestArrivalAirport.getId() == requestedDepartureAirport.getId()){
                System.out.println("Checking equals id in Long format :"+lastestArrivalAirport.getId().equals(requestedDepartureAirport.getId()));
                return true;
            }
        }

        return false;
    }

    public boolean checkPriceAvailability(Airway airway, Aircraft aircraft){



        // ki???m tra s??? c???p airway-priceByClass th???c t??? c?? b???ng s??? c???p airway_priceByClass ????ng nh??? ph???i c??
        // n???u b???ng -> return true
        // return false


        // s??? travelClass m?? m???i airway ph???i c??
        int numberOfTravelClass=travelClassService.checkNumberOfTravelClass();

        // Th???c t??? s??? c???p airway v?? travelClass
        int numOfSettedPrice = airway.getPriceByClasses().size();

//        int numOfSettedSeats = aircraft.getAircraftType().getSeatsByClassSet().size();

        if(numOfSettedPrice == numberOfTravelClass){ // ?
            return true;
        }
        return false;

    }

    // search 1 way xong
    public List<Flight> searchOneWayFlights(SearchPayload searchPayload) throws InvalidInputValueException, NoResultException {
            // one way flight
//        System.out.println("In Flight service");
        // filter b???ng location
        List<Flight>  filteredByLocationFlights = this.filterByInputAndOutputLocation(searchPayload.getDepartureAirportOrCity(),searchPayload.getArrivalAirportOrCity());

       // filter by time
         List<Flight> filteredByTimeFlights = filterByTime(filteredByLocationFlights,searchPayload);

        // flight : filter by travelclassId v?? number of traveler

        List<Flight> filteredByTravelClassAndNumberOfTravelerFlights = filterByTravelClassIdAndNumberOfTravler(filteredByTimeFlights,searchPayload.getTravelClassId(),searchPayload.getNumberOfTraveler());

         System.out.println(filteredByTravelClassAndNumberOfTravelerFlights);
        return filteredByTravelClassAndNumberOfTravelerFlights;
    }
    // filter by travelclassId v?? number of traveler
    private List<Flight> filterByTravelClassIdAndNumberOfTravler(List<Flight> flightList, Long travelClassId,int numberOfTraveler) throws NoResultException {
        List<Flight> result = new ArrayList<>();
        for(Flight flight : flightList){
            // th??m ??i???u ki???n , li???u flight v???i aircraft n??y c?? travelClass n??y kh??ng
            Long aircraftTypeId = flight.getAircraft().getId();
            // d??ng seatsByClass service n??y ????? bi???t c?? aircraft_type n??y  v???i travelClass n??y kh??ng
             boolean checkExistSeatByClass = seatsByClassService.checkExistedSeatsByClassByAircraftTypeIdAndTravelClassId(aircraftTypeId,travelClassId);
            if(!checkExistSeatByClass){
                continue;
            }
//            System.out.println("Aircraft_type_id"+flight.getAircraft().getAircraftType().getId());

            int numberOfSlot =ticketService.checkRemainNumberOfAvailableTicketForEachClass(flight.getId(),travelClassId);
            if(numberOfSlot >= numberOfTraveler){
                result.add(flight);
            }
        }
        if(result.isEmpty()){
            throw new NoResultException("Kh??ng c?? m??y bay n??o v???i travelclassid : "+travelClassId+" v?? number of travler :"+numberOfTraveler);
        }
        return result;
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
//        String  searchArrivalDate =  searchPayload.getArrivalTime().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Flight flight : filteredByLoactionFlights) {
            // Xu ly date c???a flight
            Date flightDepartureDate = Date.from(flight.getDepartureTime());
            Date flightArrivalDate = Date.from(flight.getArrivalTime());
            String flightDepartureDateString = simpleDateFormat.format(flightDepartureDate);
//            String flightArrivalDateString = simpleDateFormat.format(flightArrivalDate);
            System.out.println("bi???n flightDepartureDateString sau khi ???????c convert : "+flightDepartureDateString);
//            System.out.println("bi???n flightArrivalDateString sau khi ???????c convert : "+flightArrivalDateString);
    //&& flightArrivalDateString.equals(searchArrivalDate)
            if (flightDepartureDateString.equals(searchDepartureDate) ) {
                filteredByTimeFlights.add(flight);
            }
        }

        if(filteredByTimeFlights.isEmpty()){
            throw new NoResultException("Kh??ng c?? chuy???n bay n??o t??? :"+searchPayload.getDepartureTime()+" ?????n :"+searchPayload.getArrivalTime());
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
            // kh??ng c?? th??nh ph??? n??o nh?? v???y m?? c?? ch???a b???t k?? airport n??o
            if(!airportService.checkExistedAirportsByCityName(inputDeparturePlace)){

                throw new InvalidInputValueException(("??i???n sai th??ng tin n??i ??i: "+inputDeparturePlace));
            }
        }
        if(!airportService.checkExistedByAirportName(inputArrivalPlace)){
            arrivalPlaceIsAirport = false;
            // kh??ng c?? th??nh ph??? n??o nh?? v???y m?? c?? ch???a b???t k?? airport n??o
            if(!airportService.checkExistedAirportsByCityName(inputArrivalPlace)){
                throw new InvalidInputValueException(("??i???n sai th??ng tin n??i ?????n: "+inputArrivalPlace));
            }
        }
        System.out.println("departure la airport ?" +departurePlaceIsAirport+"arrival is a airport"+arrivalPlaceIsAirport);


        // n?? cho m??nh 2 t??n th??nh ph??? ho???c 2 t??n airport
        // case 1 : 2 t??n airport oke

        // b?????c 2 : ki???m tra c?? airway t??? airport n??y ?????n airport kia kh??ng -> l???y ???????c airway_id
        // b?????c 3 : query Flight table ????? tr??? v??? nh???ng chuy???n bay c?? airway_id nh?? n??y
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
        // case 2 :t??n 2 th??nh ph??? oke
        // b?????c 1 : ki???m tra c?? airport n??o thu???c 2 th??nh ph??? ???? kh??ng -> tr??? v??? ???????c thanhpho1[airports]+thanhpho2[airports]
        // b?????c 2 : ki???m tra gi???a c??c airport c?? t???n t???i airway n??o kh??ng -> nh???n ???????c 1 list airway
        // b?????c 3 : query Flight table ????? tr??? v??? nh???ng chuy???n bay c?? airway_id nh?? n??y
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
        //!!! v???i nh???ng flight c?? airway_Id nh?? nhau c???n c?? departure && arrival time kh??c nhau

        // case 3 : departure place l?? th??nh ph??? arrival place l?? s??n bay OK
        // b?????c 1 : ki???m tra c?? airport ??? th??nh ph??? ???? kh??ng -> nh???n ???????c List<Airport>
        // b?????c 2 : ki???m tra c?? airway t??? C??c airport ??? th??nh ph??? ???? ?????n airport n??y kh??ng-> List<Airway>
        // b?????c 3 : ki???m tra c??c chuy???n bay c?? airway_id nh?? th???

        else if((!departurePlaceIsAirport)&&arrivalPlaceIsAirport){
            System.out.println("departure is city arrival is airport");
            List<Airway> airwayList = new ArrayList<Airway>();
            // t??m ???????c c??c airport c???a  th??nh ph???
            List<Airport> departureAirportList=  airportService.getAirportsByCityName(inputDeparturePlace);
            // t??m  airport
            Airport arrivalAirport = airportService.findAirportByName(inputArrivalPlace);

            // v???i arrival airport n??y th?? c?? c??c ???????ng bay n??o t??? c??c airport c???a th??nh ph???
            for(Airport airport : departureAirportList){
                Airway airway=   airwayService.findByArrivalAirportIdAndDepartureAirportId(arrivalAirport.getId(),airport.getId());
                airwayList.add(airway);
            }


            for(Airway airway : airwayList){

                List<Flight> flights=    flightRepository.findFlightByAirway_Id(airway.getId());

                flightList.add(flights);

            }

        }

        // case 4 : departure place l?? airport v?? arrival place l?? th??nh ph??? OK
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
           throw new NoResultException("Kh??ng c?? chuy???n bay ????? ki???m tra remain slot v???i flight_id "+flightId);
       }
       Flight flight = flightToCheck.get();
       return flight.getRemainSlot();
    }

    @Transactional
    public boolean updateTimeFlight(Long id, Instant departureTime, Instant arrivalTime, Long departureGateId, Long arrivalGateId) throws InvalidInputValueException {
    	if(validateTimeAndGate(departureTime, arrivalTime, departureGateId, arrivalGateId)) {
    		Flight flight = getById(id);
    		if (validateUpdateTime(flight, arrivalTime, departureTime)){
                flight.setDepartureTime(departureTime);
                flight.setArrivalTime(arrivalTime);
                flightRepository.save(flight);
                return true;
            }
        	return false;
    	}
    	return false;
    }
    private boolean validateTimeAndGate(Instant departureTime, Instant arrivalTime, Long departureGateId, Long arrivalGateId) {
    	return !flightRepository.findByArrivalTimeAndDepartureTimeAndArrivalGate_IdAndDepartureGate_Id(arrivalTime, departureTime, arrivalGateId, departureGateId).isPresent();
    }


    public void deleteFlight(Long id){
        flightRepository.deleteById(id);
        return;
    }

    private boolean validateUpdateTime (Flight flight, Instant arrivalTime, Instant dipartureTime) throws InvalidInputValueException {
        Aircraft aircraft = flight.getAircraft();
        Long flightId = flight.getId();
        Set<Flight> flightSet = aircraft.getFlights();

        if (flightSet.size() <= 1){
            return true;
        }

        List<Flight> flightList = flightSet.stream()
                .sorted(Comparator.comparing(Flight::getId))
                .collect(Collectors.toList());



        Flight nextFlight = null;
        Flight previousFlight = null;

        int index = 0;
        for(Flight f : flightList){
            System.out.println(f.getId());
            if (f.getId() == flightId){
                break;
            }
            index++;
        }

        if (index == 0){
            nextFlight = flightList.get(index + 1);

            if (arrivalTime.compareTo(nextFlight.getDepartureTime()) > 0){
                throw new InvalidInputValueException("the given arrival time is conflict with the next flight time of this aircraft");
            }
        } else if (index == flightList.size() - 1){
            previousFlight = flightList.get(index - 1);

            if (dipartureTime.compareTo(previousFlight.getArrivalTime().plusSeconds(3600)) < 0){
                throw new InvalidInputValueException("the given departure time should be 1 hour later from the last arrival time of this aircraft");
            }
        } else {
            nextFlight = flightList.get(index + 1);
            previousFlight = flightList.get(index - 1);
            if (arrivalTime.compareTo(nextFlight.getDepartureTime()) > 0){
                throw new InvalidInputValueException("the given arrival time is conflict with the next flight time of this aircraft");
            }
            if (dipartureTime.compareTo(previousFlight.getArrivalTime().plusSeconds(3600)) < 0){
                throw new InvalidInputValueException("the given departure time should be 1 hour later from the last arrival time of this aircraft");
            }
        }

        return true;
    }

}
