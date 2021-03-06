package com.se2.hanuairline.service;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.exception.NoResultException;
import com.se2.hanuairline.model.*;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
//import com.se2.hanuairline.model.aircraft.AircraftType;
import com.se2.hanuairline.model.aircraft.SeatsByClass;
import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.payload.CartPayload;
import com.se2.hanuairline.payload.GetTicketPricePayload;
import com.se2.hanuairline.payload.TicketPayload;
import com.se2.hanuairline.repository.TaxAndMarkupRepository;
import com.se2.hanuairline.repository.TicketRepository;
import com.se2.hanuairline.security.JwtTokenProvider;
import com.se2.hanuairline.service.aircraft.AircraftSeatService;
import com.se2.hanuairline.service.aircraft.AircraftService;
import com.se2.hanuairline.service.aircraft.AircraftTypeService;
import com.se2.hanuairline.service.aircraft.SeatsByClassService;
import com.se2.hanuairline.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private AircraftSeatService aircraftSeatService;

    @Autowired
    private AircraftService aircraftService;

    @Autowired
    private SeatsByClassService seatsByClassService;

    @Autowired
    private PriceByClassService priceByClassService;

    @Autowired
    private EmailService emailService;

    @Autowired
    public TaxAndMarkupRepository taxAndMarkupRepository;

//    public int getNumberOfTicketsByFlightId(Long flight_id){
//        return ticketRepository.countByFlight_Id(flight_id);
//    }

    public Ticket createTicket (TicketPayload request) throws InvalidInputValueException, MessagingException {
        User user = userService.getUserById(request.getUser_id());
        Flight flight = flightService.getById(request.getFlight_id());
        AircraftSeat aircraftSeat = aircraftSeatService.getAircraftSeatById(request.getAircraftSeat_id());


        if (user == null || flight == null || aircraftSeat == null){
            throw new InvalidInputValueException("TicketPayLoad false!!");
        }
        //  test xem aircraftSeat n??y c?? thu???c aircraft ???? kh??ng !!

        if(!aircraftSeatService.checkSeatBelongToTheAircraft(request.getAircraftSeat_id(),request.getFlight_id())) {
            throw new InvalidInputValueException("Ticket payload false");
            }



        // check duplication !!!!!
       if(!validateUserChooseSeat(request.getFlight_id(),request.getAircraftSeat_id())){
           throw new InvalidInputValueException("V?? ???? c?? ng?????i ?????t");
       };

       List<TaxAndMarkup> taxAndMarkups = taxAndMarkupRepository.findAll();
        int rate = 0;

        for (TaxAndMarkup tax : taxAndMarkups){
            System.out.println(tax.getFarePercentage());
            int taxRate = tax.getFarePercentage();
            rate += taxRate;
        }

        int price = (int) (request.getTotalPrice());

        if (rate!= 0) {
            price = (int) (request.getTotalPrice()*rate/100);
        }


        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setAircraftSeat(aircraftSeat);
        ticket.setStatus(TicketStatus.BOOKED);
        ticket.setType(request.getType());
        ticket.setTotalPrice(request.getTotalPrice());

        Ticket _ticket = ticketRepository.save(ticket);
        emailService.verifyTicketByEmail(ticket);
        
        
        return _ticket;

    }

    public boolean deleteTicket (Long ticketId, String token){
        try {
            Optional<Ticket> ticket = ticketRepository.findById(ticketId);
            if(!ticket.isPresent()){
                return false;
            }

            Long ownerId = ticket.get().getUser().getId();
            JwtTokenProvider tokenEncoder = new JwtTokenProvider();
            Long userId = tokenEncoder.getUserIdFromJWT(token);

            if(userId != ownerId){
                return false;
            }


            ticketRepository.deleteById(ticketId);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public List<Ticket> getByUserId(Long userId){
        List<Ticket> tickets = ticketRepository.findByUser_Id(userId, Sort.by(Sort.Direction.DESC, "createdAt"));

        return tickets;
    }

    public List<Ticket> getByFlightId(Long flightId) {
        List<Ticket> tickets = ticketRepository.findTicketByFlight_Id(flightId);
        System.out.println("Tickets"+tickets);
//        if(tickets)
        return tickets;
    }
    //    public Ticket findTicketByAircraftSeatIdAndFlightId(String aircraftSeatId,Long flightId) throws NoResultException {
//        System.out.println("Aircraftseatid: "+aircraftSeatId+" flightid: " +flightId);
//        Optional<Ticket> ticket = ticketRepository.findTicketByAircraftSeat_IdAndFlight_Id(aircraftSeatId,flightId);
//        System.out.println("ticket present ? : " + ticket.isPresent());
//        if(!ticket.isPresent()){
//            throw new NoResultException("Kh??ng c?? ticket cho aircraftSeat_id :"+aircraftSeatId+" v?? flightId: "+flightId);
//        }
//        return ticket.get();
//    }

    // t??m s??? v?? c??n l???i v???i flight_id v?? travel_class_id
    // t??? flight_id -> t??m ???????c remain_slot , t??m ???????c aircraft_id,
    // t??? aircraft_id -> t??m ???????c aircraft_type_id
    // aircraft_type_id  + travelClass_id -> t??m ???????c s??? gh??? c???a travelClass c???a aircraft ????
    // query b???ng ticket t??m c??c ticket v???i flight_id -> List<Ticket>
    // loop t??? ticket_id -> t??m ???????c aircraft_seat_id -> t??m ???????c s??? l?????ng c??n l???i v???i travel_class ???? (seat v???i class )

    public int checkRemainNumberOfAvailableTicketForEachClass(Long flightId,Long travelClassId) throws NoResultException {


            int remainSlotForThisFlight =  flightService.checkRemainSlot(flightId);
            if(remainSlotForThisFlight==0){
                return  remainSlotForThisFlight;
            }
            // find aircraft_id from flight id
            // ch???c ch???n ph???i c?? aircraftId -> ch???c ch???c ph???i c?? aircraft
            Long aircraftId =    aircraftService.findAircraftIdByFlightId(flightId);

           Aircraft aircraft = aircraftService.getAircraftById(aircraftId);
           // l???y ???????c aircraft_type_id
            Long aircraftTypeId = aircraft.getAircraftType().getId();

            SeatsByClass seatsByClass =seatsByClassService.findByAircraftTypeIdAndTravelclassId(aircraftTypeId,travelClassId);
            // t??m ???????c s??? gh??? cho class ????
            int numberOfSeatsForThisClass = seatsByClass.getQuantity();

            // t??m c??c ticket v???i travel_class_id v?? flight_id nh?? v???y
        List<Ticket> ticketList =     ticketRepository.findTicketByFlight_Id(flightId);
        if(ticketList==null){
            throw new NoResultException("tiketList == null : " +flightId);
        }
        int numberOfRemainSlot = numberOfSeatsForThisClass;
        for(Ticket ticket : ticketList){
            String aircraftSeatId=ticket.getAircraftSeat().getId();
            AircraftSeat aircraftSeat =aircraftSeatService.getAircraftSeatById(aircraftSeatId);
            if(aircraftSeat.getTravelClass().getId().equals(travelClassId)){
                numberOfRemainSlot--;
            }

        }



        return numberOfRemainSlot;
    }
    // b1 : lay ticket tu flight_id
    // b2 : lap qua cac ticket
    // if ticket.getid==seatId ng dung nhap - > false
    // return true
    public boolean validateUserChooseSeat(Long flightId, String seatId){

        boolean result = true;
        List<Ticket> ticketList = this.getByFlightId(flightId);
        for(Ticket ticket : ticketList){
            if(ticket.getAircraftSeat().getId().equals(seatId)){
                result = false;
                break;
            }
        }
        return result;


    }

    public List<TicketPayload> getTicketPrice(CartPayload request) throws InvalidInputValueException {
        List<TicketPayload> results = new ArrayList<TicketPayload>();

        for(GetTicketPricePayload requestTicket : request.getGetTicketPricePayloads()){
            String travelClassName = requestTicket.getSeatId().substring(0,1);
            PriceByClass priceByClass = priceByClassService.getPriceByClassByClassNameAndAirwayId(travelClassName, request.getAirwayId());
            int price = priceByClass.getPrice();

            if(requestTicket.getTicketType().equals(TicketType.CHILDREN)){
                price = (int) (price * 70 / 100);
            }

            List<TaxAndMarkup> taxAndMarkups = taxAndMarkupRepository.findAll();
            int rate = 0;

            for (TaxAndMarkup tax : taxAndMarkups){
                int taxRate = tax.getFarePercentage();
                rate += taxRate;
            }

            if (rate != 0){
                price = (int) (price*rate/100);
            }


            TicketPayload ticketPayload = new TicketPayload();
            ticketPayload.setFlight_id(request.getFlightId());
            ticketPayload.setStatus(TicketStatus.AVAILABLE);
            ticketPayload.setOrder_id(null);
            ticketPayload.setUser_id(request.getUserId());
            ticketPayload.setType(requestTicket.getTicketType());
            ticketPayload.setAircraftSeat_id(requestTicket.getSeatId());
            ticketPayload.setTotalPrice(price);
            ticketPayload.setId(0L);

            results.add(ticketPayload);
        }

        return results;
    }

}
