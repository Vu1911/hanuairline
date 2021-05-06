package com.se2.hanuairline.controller.airport;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se2.hanuairline.model.Flight;
import com.se2.hanuairline.model.PriceByClass;
import com.se2.hanuairline.model.airport.Airport;
import com.se2.hanuairline.model.airport.AirportStatus;
import com.se2.hanuairline.model.airport.Airway;
import com.se2.hanuairline.model.airport.Gate;
import com.se2.hanuairline.payload.airport.AirwayPayload;
import com.se2.hanuairline.repository.airport.AirportRepository;
import com.se2.hanuairline.repository.airport.AirwayRepository;
import com.se2.hanuairline.service.airport.AirwayService;

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

@ContextConfiguration(classes = {AirwayController.class})
@ExtendWith(SpringExtension.class)
public class AirwayControllerTest {
    @MockBean
    private AirportRepository airportRepository;

    @Autowired
    private AirwayController airwayController;

    @MockBean
    private AirwayRepository airwayRepository;

    @MockBean
    private AirwayService airwayService;

    @Test
    public void testCreateAirway() throws Exception {
        AirwayPayload airwayPayload = new AirwayPayload();
        airwayPayload.setArrival_airport_id(42L);
        airwayPayload.setDeparture_airport_id(1L);
        airwayPayload.setId(123L);
        airwayPayload.setDistance_km(1);
        String content = (new ObjectMapper()).writeValueAsString(airwayPayload);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/airway/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.airwayController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("Duplicate airway or wrong airport id. Maybe logic error")));
    }

    @Test
    public void testCreateAirway2() throws Exception {
        AirwayPayload airwayPayload = new AirwayPayload();
        airwayPayload.setArrival_airport_id(42L);
        airwayPayload.setDeparture_airport_id(1L);
        airwayPayload.setId(123L);
        airwayPayload.setDistance_km(1);
        String content = (new ObjectMapper()).writeValueAsString(airwayPayload);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/airway/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.airwayController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("Duplicate airway or wrong airport id. Maybe logic error")));
    }

    @Test
    public void testDeleteAirway() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/airway/admin/delete/{id}",
                12345678987654321L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.airwayController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("wrong airway id or airway being deployed. Maybe logic error")));
    }

    @Test
    public void testDeleteAirway2() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/airway/admin/delete/{id}",
                12345678987654321L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.airwayController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("wrong airway id or airway being deployed. Maybe logic error")));
    }

    @Test
    public void testDeleteAirway3() throws Exception {
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/airway/admin/delete/{id}",
                12345678987654321L);
        deleteResult.contentType("Not all who wander are lost");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.airwayController)
                .build()
                .perform(deleteResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("wrong airway id or airway being deployed. Maybe logic error")));
    }

    @Test
    public void testFindByCities() throws Exception {
        Airport airport = new Airport();
        airport.setCapacity(3);
        airport.setGates(new HashSet<Gate>());
        airport.setCountry("Country");
        airport.setCity("Oxford");
        airport.setId(123L);
        airport.setName("Name");
        airport.setStatus(AirportStatus.OPENED);
        airport.setAirway1(new HashSet<Airway>());
        airport.setAirway2(new HashSet<Airway>());

        Airport airport1 = new Airport();
        airport1.setCapacity(3);
        airport1.setGates(new HashSet<Gate>());
        airport1.setCountry("Country");
        airport1.setCity("Oxford");
        airport1.setId(123L);
        airport1.setName("Name");
        airport1.setStatus(AirportStatus.OPENED);
        airport1.setAirway1(new HashSet<Airway>());
        airport1.setAirway2(new HashSet<Airway>());

        Airway airway = new Airway();
        airway.setArrivalAirport(airport);
        airway.setDepartureAirport(airport1);
        airway.setPriceByClasses(new HashSet<PriceByClass>());
        airway.setId(123L);
        airway.setDistanceKm(1);
        airway.setFlight(new HashSet<Flight>());
        when(this.airwayService.findByArrivalCityAndDepartureCity(anyString(), anyString())).thenReturn(airway);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/airway/findByCities")
                .param("arrivalCityName", "foo")
                .param("departureCityName", "foo");
        MockMvcBuilders.standaloneSetup(this.airwayController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "<Airway><id>123</id><departureAirport><id>123</id><name>Name</name><country>Country</country><city"
                                        + ">Oxford</city><capacity>3</capacity><status>OPENED</status><gates/></departureAirport><arrivalAirport"
                                        + "><id>123</id><name>Name</name><country>Country</country><city>Oxford</city><capacity>3</capacity>"
                                        + "<status>OPENED</status><gates/></arrivalAirport><distanceKm>1</distanceKm><priceByClasses/></Airway>")));
    }

    @Test
    public void testFindByCities2() throws Exception {
        Airport airport = new Airport();
        airport.setCapacity(3);
        airport.setGates(new HashSet<Gate>());
        airport.setCountry("Country");
        airport.setCity("Oxford");
        airport.setId(123L);
        airport.setName("Name");
        airport.setStatus(AirportStatus.OPENED);
        airport.setAirway1(new HashSet<Airway>());
        airport.setAirway2(new HashSet<Airway>());

        Airport airport1 = new Airport();
        airport1.setCapacity(3);
        airport1.setGates(new HashSet<Gate>());
        airport1.setCountry("Country");
        airport1.setCity("Oxford");
        airport1.setId(123L);
        airport1.setName("Name");
        airport1.setStatus(AirportStatus.OPENED);
        airport1.setAirway1(new HashSet<Airway>());
        airport1.setAirway2(new HashSet<Airway>());

        Airway airway = new Airway();
        airway.setArrivalAirport(airport);
        airway.setDepartureAirport(airport1);
        airway.setPriceByClasses(new HashSet<PriceByClass>());
        airway.setId(123L);
        airway.setDistanceKm(1);
        airway.setFlight(new HashSet<Flight>());
        when(this.airwayService.findByArrivalCityAndDepartureCity(anyString(), anyString())).thenReturn(airway);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/airway/findByCities")
                .param("arrivalCityName", "foo")
                .param("departureCityName", "foo");
        MockMvcBuilders.standaloneSetup(this.airwayController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "<Airway><id>123</id><departureAirport><id>123</id><name>Name</name><country>Country</country><city"
                                        + ">Oxford</city><capacity>3</capacity><status>OPENED</status><gates/></departureAirport><arrivalAirport"
                                        + "><id>123</id><name>Name</name><country>Country</country><city>Oxford</city><capacity>3</capacity>"
                                        + "<status>OPENED</status><gates/></arrivalAirport><distanceKm>1</distanceKm><priceByClasses/></Airway>")));
    }

    @Test
    public void testGetAllAirway() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/airway/getAll");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("size", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult1.param("sort",
                String.valueOf(new String[]{"foo", "foo", "foo"}));
        MockMvcBuilders.standaloneSetup(this.airwayController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAllAirway2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/airway/getAll");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("size", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult1.param("sort",
                String.valueOf(new String[]{"foo", "foo", "foo"}));
        MockMvcBuilders.standaloneSetup(this.airwayController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

