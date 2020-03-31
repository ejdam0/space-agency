package pl.strzelecki.spaceagency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.strzelecki.spaceagency.model.DTO.OrderDTO;
import pl.strzelecki.spaceagency.model.DTO.TopMissionDTO;
import pl.strzelecki.spaceagency.model.DTO.TopProductDTO;
import pl.strzelecki.spaceagency.model.Order;
import pl.strzelecki.spaceagency.model.Person;
import pl.strzelecki.spaceagency.service.OrderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderRestController orderRestController;

    MockMvc mockMvc;
    OrderDTO orderDTO;
    Person person;
    String requestJson;
    Map<Long, Integer> productsIdsAndQuantities = new HashMap<>();

    @BeforeEach
    void setUp() {
        person = new Person(1L,
                "Test-fName",
                "Test-lName",
                "Test-auth",
                "Test-user",
                "Test-pass",
                true);
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController).build();
    }

    @Test
    void add() throws Exception {
        productsIdsAndQuantities.put(1L, 1);
        orderDTO = new OrderDTO(person, productsIdsAndQuantities);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        requestJson = ow.writeValueAsString(orderDTO);

        mockMvc.perform(post("/orders/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }
}