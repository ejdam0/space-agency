package pl.strzelecki.spaceagency.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import pl.strzelecki.spaceagency.model.ImageryTypeEnum;
import pl.strzelecki.spaceagency.model.Mission;
import pl.strzelecki.spaceagency.service.AgencyService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MissionRestControllerTest {

    @Mock
    AgencyService<Mission> missionService;

    @InjectMocks
    MissionRestController missionRestController;

    MockMvc mockMvc;
    Mission mission;
    String requestJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mission = new Mission(1,
                "Test-mission-name",
                ImageryTypeEnum.HYPERSPECTRAL,
                LocalDate.now(),
                LocalDate.now());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        requestJson = ow.writeValueAsString(mission);

        mockMvc = MockMvcBuilders.standaloneSetup(missionRestController).build();
    }

    @Test
    void add() throws Exception {
        mockMvc.perform(post("/missions/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    void edit() throws Exception {
        mockMvc.perform(put("/missions/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}