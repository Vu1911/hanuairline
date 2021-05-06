package com.se2.hanuairline.controller.aircraft;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.model.aircraft.AircraftStatus;
import com.se2.hanuairline.model.aircraft.AircraftType;
import com.se2.hanuairline.model.aircraft.SeatsByClass;
import com.se2.hanuairline.model.aircraft.TravelClass;
import com.se2.hanuairline.service.aircraft.AircraftSeatService;

import java.util.ArrayList;

import java.util.HashSet;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AircraftSeatController.class})
@ExtendWith(SpringExtension.class)
public class AircraftSeatControllerTest {
    @Autowired
    private AircraftSeatController aircraftSeatController;

    @MockBean
    private AircraftSeatService aircraftSeatService;

    @Test
    public void testGetAircraftSeatById() throws Exception {
        TravelClass travelClass = new TravelClass();
        travelClass.setId(123L);
        travelClass.setName("Name");
        travelClass.setDescription("The characteristics of someone or something");

        AircraftType aircraftType = new AircraftType();
        aircraftType.setCreatedAt(null);
        aircraftType.setSeatCapacity(1);
        aircraftType.setId(123L);
        aircraftType.setName("Name");
        aircraftType.setSeatsByClassSet(new HashSet<SeatsByClass>());
        aircraftType.setAverageVelocity(1);
        aircraftType.setAircraft(new HashSet<Aircraft>());
        aircraftType.setUpdatedAt(null);
        aircraftType.setLuggageCapacityKg(1);

        Aircraft aircraft = new Aircraft();
        aircraft.setCreatedAt(null);
        aircraft.setStatus(AircraftStatus.ACTIVATED);
        aircraft.setFlights(new HashSet<Flight>());
        aircraft.setId(123L);
        aircraft.setAircraftSeatSet(new HashSet<AircraftSeat>());
        aircraft.setName("Name");
        aircraft.setAircraftType(aircraftType);
        aircraft.setUpdatedAt(null);

        AircraftSeat aircraftSeat = new AircraftSeat();
        aircraftSeat.setCreatedAt(null);
        aircraftSeat.setId("42");
        aircraftSeat.setTravelClass(travelClass);
        aircraftSeat.setAircraft(aircraft);
        aircraftSeat.setUpdatedAt(null);
        when(this.aircraftSeatService.getAircraftSeatById(anyString())).thenReturn(aircraftSeat);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/aircraftSeat/admin/getById/{id}",
                "value");
        MockMvcBuilders.standaloneSetup(this.aircraftSeatController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "<AircraftSeat><createdAt/><updatedAt/><id>42</id><travelClass><id>123</id><name>Name</name><description>The"
                                        + " characteristics of someone or something</description></travelClass><aircraft><createdAt/><updatedAt"
                                        + "/><id>123</id><name>Name</name><aircraftType><createdAt/><updatedAt/><id>123</id><name>Name</name>"
                                        + "<seatCapacity>1</seatCapacity><luggageCapacityKg>1</luggageCapacityKg><averageVelocity>1</averageVelocity"
                                        + "></aircraftType><status>ACTIVATED</status></aircraft></AircraftSeat>")));
    }

    @Test
    public void testGetAllAircraftSeat() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/aircraftSeat/admin/getAll");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("size", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult1.param("sort",
                String.valueOf(new String[]{"foo", "foo", "foo"}));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.aircraftSeatController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }

    @Test
    public void testGetByAircraftId() throws Exception {
        when(this.aircraftSeatService.getByAircrafId((Long) any())).thenReturn(new ArrayList<AircraftSeat>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/aircraftSeat/admin/getByAircraftId/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.aircraftSeatController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(
                        MockMvcResultMatchers.content().string(Matchers.containsString("Check aircraftSeatId. Maybe logic error")));
    }

    @Test
    public void testGetByAircraftId2() throws Exception {
        TravelClass travelClass = new TravelClass();
        travelClass.setId(123L);
        travelClass.setName("?");
        travelClass.setDescription("The characteristics of someone or something");

        AircraftType aircraftType = new AircraftType();
        aircraftType.setCreatedAt(null);
        aircraftType.setSeatCapacity(0);
        aircraftType.setId(123L);
        aircraftType.setName("?");
        aircraftType.setSeatsByClassSet(new HashSet<SeatsByClass>());
        aircraftType.setAverageVelocity(0);
        aircraftType.setAircraft(new HashSet<Aircraft>());
        aircraftType.setUpdatedAt(null);
        aircraftType.setLuggageCapacityKg(0);

        Aircraft aircraft = new Aircraft();
        aircraft.setCreatedAt(null);
        aircraft.setStatus(AircraftStatus.ACTIVATED);
        aircraft.setFlights(new HashSet<Flight>());
        aircraft.setId(123L);
        aircraft.setAircraftSeatSet(new HashSet<AircraftSeat>());
        aircraft.setName("?");
        aircraft.setAircraftType(aircraftType);
        aircraft.setUpdatedAt(null);

        AircraftSeat aircraftSeat = new AircraftSeat();
        aircraftSeat.setCreatedAt(null);
        aircraftSeat.setId("42");
        aircraftSeat.setTravelClass(travelClass);
        aircraftSeat.setAircraft(aircraft);
        aircraftSeat.setUpdatedAt(null);

        ArrayList<AircraftSeat> aircraftSeatList = new ArrayList<AircraftSeat>();
        aircraftSeatList.add(aircraftSeat);
        when(this.aircraftSeatService.getByAircrafId((Long) any())).thenReturn(aircraftSeatList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/aircraftSeat/admin/getByAircraftId/{id}", 1L);
        MockMvcBuilders.standaloneSetup(this.aircraftSeatController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "<ArrayList><item><createdAt/><updatedAt/><id>42</id><travelClass><id>123</id><name>?</name><description>The"
                                        + " characteristics of someone or something</description></travelClass><aircraft><createdAt/><updatedAt"
                                        + "/><id>123</id><name>?</name><aircraftType><createdAt/><updatedAt/><id>123</id><name>?</name><seatCapacity"
                                        + ">0</seatCapacity><luggageCapacityKg>0</luggageCapacityKg><averageVelocity>0</averageVelocity><"
                                        + "/aircraftType><status>ACTIVATED</status></aircraft></item></ArrayList>")));
    }
}

