package com.se2.hanuairline.controller.aircraft;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.se2.hanuairline.model.aircraft.Aircraft;
import com.se2.hanuairline.model.aircraft.AircraftType;
import com.se2.hanuairline.model.aircraft.SeatsByClass;
import com.se2.hanuairline.service.aircraft.AircraftTypeService;

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

@ContextConfiguration(classes = {AircraftTypeController.class})
@ExtendWith(SpringExtension.class)
public class AircraftTypeControllerTest {
    @Autowired
    private AircraftTypeController aircraftTypeController;

    @MockBean
    private AircraftTypeService aircraftTypeService;

    @Test
    public void testGetAircraftTypeById() throws Exception {
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
        when(this.aircraftTypeService.getAircraftTypeById((Long) any())).thenReturn(aircraftType);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/aircraftType/getById/{id}",
                12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.aircraftTypeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "<AircraftType><createdAt/><updatedAt/><id>123</id><name>Name</name><seatCapacity>1</seatCapacity>"
                                        + "<luggageCapacityKg>1</luggageCapacityKg><averageVelocity>1</averageVelocity></AircraftType>")));
    }

    @Test
    public void testDeleteAircraftType() throws Exception {
        when(this.aircraftTypeService.deleteAircraftType(anyLong())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/aircraftType/admin/delete/{id}",
                12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.aircraftTypeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAllAircraftType() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/aircraftType/getAll");
        MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("size", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult1.param("sort",
                String.valueOf(new String[]{"foo", "foo", "foo"}));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.aircraftTypeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}

