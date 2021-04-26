package com.se2.hanuairline.service;

import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.exception.NoResultException;
import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.Ticket;
import com.se2.hanuairline.model.TicketStatus;
import com.se2.hanuairline.model.TicketType;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
//import com.se2.hanuairline.model.aircraft.AircraftType;
import com.se2.hanuairline.model.aircraft.SeatsByClass;
import com.se2.hanuairline.model.user.User;
import com.se2.hanuairline.payload.TicketPayload;
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
    private EmailService emailService;

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
        //  test xem aircraftSeat này có thuộc aircraft đó không !!

        if(!aircraftSeatService.checkSeatBelongToTheAircraft(request.getAircraftSeat_id(),request.getFlight_id())) {
            throw new InvalidInputValueException("Ticket payload false");
            }



        // check duplication !!!!!
       if(!validateUserChooseSeat(request.getFlight_id(),request.getAircraftSeat_id())){
           throw new InvalidInputValueException("Vé đã có người đặt");
       };

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setAircraftSeat(aircraftSeat);
        ticket.setStatus(TicketStatus.BOOKED);
        ticket.setType(request.getType());

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
//            throw new NoResultException("Không có ticket cho aircraftSeat_id :"+aircraftSeatId+" và flightId: "+flightId);
//        }
//        return ticket.get();
//    }

    // tìm số vé còn lại với flight_id và travel_class_id
    // từ flight_id -> tìm được remain_slot , tìm được aircraft_id,
    // từ aircraft_id -> tìm được aircraft_type_id
    // aircraft_type_id  + travelClass_id -> tìm được số ghế của travelClass của aircraft đó
    // query bảng ticket tìm các ticket với flight_id -> List<Ticket>
    // loop từ ticket_id -> tìm được aircraft_seat_id -> tìm được số lượng còn lại với travel_class đó (seat với class )

    public int checkRemainNumberOfAvailableTicketForEachClass(Long flightId,Long travelClassId) throws NoResultException {


            int remainSlotForThisFlight =  flightService.checkRemainSlot(flightId);
            if(remainSlotForThisFlight==0){
                return  remainSlotForThisFlight;
            }
            // find aircraft_id from flight id
            // chắc chắn phải có aircraftId -> chắc chắc phải có aircraft
            Long aircraftId =    aircraftService.findAircraftIdByFlightId(flightId);

           Aircraft aircraft = aircraftService.getAircraftById(aircraftId);
           // lấy được aircraft_type_id
            Long aircraftTypeId = aircraft.getAircraftType().getId();

            SeatsByClass seatsByClass =seatsByClassService.findByAircraftTypeIdAndTravelclassId(aircraftTypeId,travelClassId);
            // tìm được số ghế cho class đó
            int numberOfSeatsForThisClass = seatsByClass.getQuantity();

            // tìm các ticket với travel_class_id và flight_id như vậy
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


}
