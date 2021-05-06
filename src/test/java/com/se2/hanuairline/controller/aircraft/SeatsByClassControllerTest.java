package com.se2.hanuairline.controller.aircraft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se2.hanuairline.payload.aircraft.SeatsByClassPayLoad;
import com.se2.hanuairline.service.aircraft.SeatsByClassService;
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

@ContextConfiguration(classes = {SeatsByClassController.class})
@ExtendWith(SpringExtension.class)
public class SeatsByClassControllerTest {
    @Autowired
    private SeatsByClassController seatsByClassController;

    @MockBean
    private SeatsByClassService seatsByClassService;

    @Test
    public void testCreateSeatsByClass() throws Exception {
        SeatsByClassPayLoad seatsByClassPayLoad = new SeatsByClassPayLoad();
        seatsByClassPayLoad.setAircraftType_id(1L);
        seatsByClassPayLoad.setTravelClass_id(1L);
        seatsByClassPayLoad.setRows_quantity(1);
        seatsByClassPayLoad.setQuantity(1);
        String content = (new ObjectMapper()).writeValueAsString(seatsByClassPayLoad);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/seatsByClass/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.seatsByClassController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("check aircraft type, travel class id. Maybe logic error")));
    }

    @Test
    public void testGetAllSeatsByClass() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/seatsByClass/getAll");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("size", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult1.param("sort",
                String.valueOf(new String[]{"foo", "foo", "foo"}));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.seatsByClassController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }

    @Test
    public void testGetSeatsByClassById() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/seatsByClass/admin/getById/{id}",
                12345678987654321L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.seatsByClassController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("SeatsByClassController: seat by class not found")));
    }

    @Test
    public void testGetSeatsByClassById2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/seatsByClass/admin/getById/{id}",
                12345678987654321L);
        getResult.contentType("Not all who wander are lost");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.seatsByClassController)
                .build()
                .perform(getResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("SeatsByClassController: seat by class not found")));
    }

    @Test
    public void testUpdateSeatsByClass() throws Exception {
        SeatsByClassPayLoad seatsByClassPayLoad = new SeatsByClassPayLoad();
        seatsByClassPayLoad.setAircraftType_id(1L);
        seatsByClassPayLoad.setTravelClass_id(1L);
        seatsByClassPayLoad.setRows_quantity(1);
        seatsByClassPayLoad.setQuantity(1);
        String content = (new ObjectMapper()).writeValueAsString(seatsByClassPayLoad);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/seatsByClass/admin/updateById/{id}", 12345678987654321L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.seatsByClassController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(304))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("check updated fields again. Maybe logic error")));
    }
}

