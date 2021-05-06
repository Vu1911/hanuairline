package com.se2.hanuairline.controller.aircraft;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftSeat;
import com.se2.hanuairline.model.aircraft.AircraftStatus;
import com.se2.hanuairline.model.aircraft.AircraftType;
import com.se2.hanuairline.model.aircraft.SeatsByClass;
import com.se2.hanuairline.payload.aircraft.AircraftPayload;
import com.se2.hanuairline.service.aircraft.AircraftService;

import java.util.HashSet;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AircraftController.class})
@ExtendWith(SpringExtension.class)
public class AircraftControllerTest {
    @Autowired
    private AircraftController aircraftController;

    @MockBean
    private AircraftService aircraftService;

    @Test
    public void testGetAircraftById() throws Exception {
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
        when(this.aircraftService.getAircraftById((Long) any())).thenReturn(aircraft);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/aircraft/getById/{id}",
                12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.aircraftController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "<Aircraft><createdAt/><updatedAt/><id>123</id><name>Name</name><aircraftType><createdAt/><updatedAt/"
                                        + "><id>123</id><name>Name</name><seatCapacity>1</seatCapacity><luggageCapacityKg>1</luggageCapacityKg>"
                                        + "<averageVelocity>1</averageVelocity></aircraftType><status>ACTIVATED</status></Aircraft>")));
    }

    @Test
    public void testCreateAircraft() throws Exception {
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
        when(this.aircraftService.createAircraft((AircraftPayload) any())).thenReturn(aircraft);

        AircraftPayload aircraftPayload = new AircraftPayload();
        aircraftPayload.setAircraft_type_id(1L);
        aircraftPayload.setStatus("Status");
        aircraftPayload.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(aircraftPayload);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/aircraft/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.aircraftController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "<Aircraft><createdAt/><updatedAt/><id>123</id><name>Name</name><aircraftType><createdAt/><updatedAt/"
                                        + "><id>123</id><name>Name</name><seatCapacity>1</seatCapacity><luggageCapacityKg>1</luggageCapacityKg>"
                                        + "<averageVelocity>1</averageVelocity></aircraftType><status>ACTIVATED</status></Aircraft>")));
    }

    @Test
    public void testUpdateAircraft() throws Exception {
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
        when(this.aircraftService.updateAircraft(anyLong(), (AircraftPayload) any())).thenReturn(aircraft);

        AircraftPayload aircraftPayload = new AircraftPayload();
        aircraftPayload.setAircraft_type_id(1L);
        aircraftPayload.setStatus("Status");
        aircraftPayload.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(aircraftPayload);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/aircraft/admin/updateById/{id}", 12345678987654321L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.aircraftController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "<Aircraft><createdAt/><updatedAt/><id>123</id><name>Name</name><aircraftType><createdAt/><updatedAt/"
                                        + "><id>123</id><name>Name</name><seatCapacity>1</seatCapacity><luggageCapacityKg>1</luggageCapacityKg>"
                                        + "<averageVelocity>1</averageVelocity></aircraftType><status>ACTIVATED</status></Aircraft>")));
    }

    @Test
    public void testDeleteAircraft() throws Exception {
        when(this.aircraftService.deleteAircraft(anyLong())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/aircraft/admin/delete/{id}",
                12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.aircraftController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Deactivate success!")));
    }

    @Test
    public void testDeleteAircraft2() throws Exception {
        when(this.aircraftService.deleteAircraft(anyLong())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/aircraft/admin/delete/{id}",
                12345678987654321L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.aircraftController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(
                        MockMvcResultMatchers.content().string(Matchers.containsString("Check aircrat_id. Maybe logic error")));
    }

    @Test
    public void testDeleteAircraft3() throws Exception {
        when(this.aircraftService.deleteAircraft(anyLong())).thenReturn(true);
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/aircraft/admin/delete/{id}",
                12345678987654321L);
        deleteResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.aircraftController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Deactivate success!")));
    }

    @Test
    public void testGetAllAirCraft() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/aircraft/getAll");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("size", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult1.param("sort",
                String.valueOf(new String[]{"foo", "foo", "foo"}));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.aircraftController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }

    @Test
    public void testGetFlightHistoryById() throws Exception {
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
        when(this.aircraftService.getAircraftById((Long) any())).thenReturn(aircraft);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/aircraft/getFlightHistoryById/{id}",
                12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.aircraftController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("<HashSet/>")));
    }
}

