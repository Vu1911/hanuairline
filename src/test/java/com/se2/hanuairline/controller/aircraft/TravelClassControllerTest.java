package com.se2.hanuairline.controller.aircraft;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se2.hanuairline.exception.InvalidInputValueException;
import com.se2.hanuairline.model.aircraft.TravelClass;
import com.se2.hanuairline.payload.aircraft.TravelClassPayload;
import com.se2.hanuairline.service.aircraft.TravelClassService;

import java.util.ArrayList;

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

@ContextConfiguration(classes = {TravelClassController.class})
@ExtendWith(SpringExtension.class)
public class TravelClassControllerTest {
    @Autowired
    private TravelClassController travelClassController;

    @MockBean
    private TravelClassService travelClassService;

    @Test
    public void testCreateTravelClass() throws Exception {
        TravelClassPayload travelClassPayload = new TravelClassPayload();
        travelClassPayload.setId(123L);
        travelClassPayload.setName("Name");
        travelClassPayload.setDescription("The characteristics of someone or something");
        String content = (new ObjectMapper()).writeValueAsString(travelClassPayload);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/travelClass/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.travelClassController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAllTravelClass() throws Exception {
        when(this.travelClassService.getAllTravelClass()).thenReturn(new ArrayList<TravelClass>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/travelClass/getAll");
        MockMvcBuilders.standaloneSetup(this.travelClassController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("<ArrayList/>")));
    }

    @Test
    public void testDeleteATravelClass() throws Exception {
        TravelClass travelClass = new TravelClass();
        travelClass.setId(123L);
        travelClass.setName("Name");
        travelClass.setDescription("The characteristics of someone or something");
        when(this.travelClassService.deleteARecordById((Long) any())).thenReturn(travelClass);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/travelClass/admin/delete/{id}",
                12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.travelClassController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "<TravelClass><id>123</id><name>Name</name><description>The characteristics of someone or something<"
                                        + "/description></TravelClass>")));
    }

    @Test
    public void testDeleteATravelClass2() throws Exception {
        when(this.travelClassService.deleteARecordById((Long) any()))
                .thenThrow(new InvalidInputValueException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/travelClass/admin/delete/{id}",
                12345678987654321L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.travelClassController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(417))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("An error occurred")));
    }

    @Test
    public void testGetTravelClassById() throws Exception {
        TravelClass travelClass = new TravelClass();
        travelClass.setId(123L);
        travelClass.setName("Name");
        travelClass.setDescription("The characteristics of someone or something");
        when(this.travelClassService.getRecordById((Long) any())).thenReturn(travelClass);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/travelClass/getById/{id}",
                12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.travelClassController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "<TravelClass><id>123</id><name>Name</name><description>The characteristics of someone or something<"
                                        + "/description></TravelClass>")));
    }

    @Test
    public void testGetTravelClassById2() throws Exception {
        when(this.travelClassService.getRecordById((Long) any()))
                .thenThrow(new InvalidInputValueException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/travelClass/getById/{id}",
                12345678987654321L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.travelClassController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(417))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("An error occurred")));
    }

    @Test
    public void testUpdateTravelClass() throws Exception {
        TravelClassPayload travelClassPayload = new TravelClassPayload();
        travelClassPayload.setId(123L);
        travelClassPayload.setName("Name");
        travelClassPayload.setDescription("The characteristics of someone or something");
        String content = (new ObjectMapper()).writeValueAsString(travelClassPayload);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/travelClass/updateById/{id}", 12345678987654321L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.travelClassController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

