package pl.strzelecki.spaceagency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.strzelecki.spaceagency.model.DTO.ProductByMissionDTO;
import pl.strzelecki.spaceagency.model.DTO.ProductByTypeOrDateDTO;
import pl.strzelecki.spaceagency.model.ImageryTypeEnum;
import pl.strzelecki.spaceagency.model.Mission;
import pl.strzelecki.spaceagency.model.Product;
import pl.strzelecki.spaceagency.service.ProductService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductRestController productRestController;

    MockMvc mockMvc;

    Mission mission;
    Product product;
    List<Product> products = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mission = new Mission(1,
                "Test-mission-name",
                ImageryTypeEnum.HYPERSPECTRAL,
                LocalDate.now(),
                LocalDate.now());

        product = new Product(1L,
                mission,
                LocalDate.now(),
                "Test-product-footprint",
                1D,
                "Test-product-url");
        products.add(product);

        mockMvc = MockMvcBuilders.standaloneSetup(productRestController).build();
    }

    @Test
    void add() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(product);

        mockMvc.perform(post("/products/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    void remove() throws Exception {
        productService.getProduct(product.getId());
        productService.remove(product.getId());
        mockMvc.perform(delete("/products/delete/" + product.getId()))
                .andDo(print())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Nested
    class TestByArgs {

        Mission mission;
        ProductByMissionDTO productByMissionDTO;
        ProductByTypeOrDateDTO productByTypeOrDateDTO;

        List<ProductByMissionDTO> productByMissionDTOList = new ArrayList<>();
        List<ProductByTypeOrDateDTO> productByTypeOrDateDTOList = new ArrayList<>();

        @BeforeEach
        void setUp() {
            mission = new Mission(1,
                    "Test-name",
                    ImageryTypeEnum.HYPERSPECTRAL,
                    LocalDate.now(),
                    LocalDate.now());

            mockMvc = MockMvcBuilders.standaloneSetup(productRestController).build();
        }

        @Test
        void findByMissionName() throws Exception {
            productByMissionDTO = ProductByMissionDTO.builder()
                    .id(1L)
                    .acquisitionDate(LocalDate.now())
                    .footprint("Test-footprint")
                    .price(1D)
                    .url("Test-url")
                    .build();
            productByMissionDTOList.add(productByMissionDTO);

            given(productService.findAllByMissionName(mission.getName())).willReturn(productByMissionDTOList);

            mockMvc.perform(get("/products/search-by-name")
                    .param("mission-name", mission.getName()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.[0].id", is(Math.toIntExact(productByMissionDTO.getId()))))
                    .andExpect(jsonPath("$.[0].footprint", is(productByMissionDTO.getFootprint())))
                    .andExpect(jsonPath("$.[0].price", is(productByMissionDTO.getPrice())));
        }

        @Nested
        class TestByTypeOrDate {
            @BeforeEach
            void setUp() {
                productByTypeOrDateDTO = ProductByTypeOrDateDTO.builder()
                        .id(1L)
                        .acquisitionDate(LocalDate.of(2050, 1, 1))
                        .footprint("Test-footprint")
                        .price(1D)
                        .url("Test-url")
                        .missionName(mission.getName())
                        .build();
                productByTypeOrDateDTOList.add(productByTypeOrDateDTO);

                mockMvc = MockMvcBuilders.standaloneSetup(productRestController).build();
            }

            @Test
            void findByProductType() throws Exception {
                given(productService.findAllByProductType(mission.getImageryType().name())).willReturn(productByTypeOrDateDTOList);
                mockMvc.perform(get("/products/search-by-type")
                        .param("product-type", mission.getImageryType().name()))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.[0].id", is(Math.toIntExact(productByTypeOrDateDTO.getId()))))
                        .andExpect(jsonPath("$.[0].footprint", is(productByTypeOrDateDTO.getFootprint())))
                        .andExpect(jsonPath("$.[0].price", is(productByTypeOrDateDTO.getPrice())))
                        .andExpect(jsonPath("$.[0].missionName", is(productByTypeOrDateDTO.getMissionName())));
            }

            @Test
            void findAllLowerThanAcquisitionDate() throws Exception {
                given(productService.findAllLowerThanAcquisitionDate(LocalDate.now()))
                        .willReturn(productByTypeOrDateDTOList);

                mockMvc.perform(get("/products/search-by-date")
                        .param("flag", "LT")
                        .param("first-date", LocalDate.now().toString()))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.[0].id", is(Math.toIntExact(productByTypeOrDateDTO.getId()))))
                        .andExpect(jsonPath("$.[0].footprint", is(productByTypeOrDateDTO.getFootprint())))
                        .andExpect(jsonPath("$.[0].price", is(productByTypeOrDateDTO.getPrice())))
                        .andExpect(jsonPath("$.[0].missionName", is(productByTypeOrDateDTO.getMissionName())));
            }

            @Test
            void findAllGreaterThanAcquisitionDate() throws Exception {
                given(productService.findAllGreaterThanAcquisitionDate(LocalDate.now()))
                        .willReturn(productByTypeOrDateDTOList);

                mockMvc.perform(get("/products/search-by-date")
                        .param("flag", "GT")
                        .param("first-date", LocalDate.now().toString()))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.[0].id", is(Math.toIntExact(productByTypeOrDateDTO.getId()))))
                        .andExpect(jsonPath("$.[0].footprint", is(productByTypeOrDateDTO.getFootprint())))
                        .andExpect(jsonPath("$.[0].price", is(productByTypeOrDateDTO.getPrice())))
                        .andExpect(jsonPath("$.[0].missionName", is(productByTypeOrDateDTO.getMissionName())));
            }

            @Test
            void findAllBetweenAcquisitionDates() throws Exception {
                given(productService.findAllBetweenAcquisitionDates(
                        LocalDate.of(1900, 1, 1),
                        LocalDate.of(2200, 1, 1)))
                        .willReturn(productByTypeOrDateDTOList);

                mockMvc.perform(get("/products/search-by-date")
                        .param("flag", "LT")
                        .param("first-date", LocalDate.of(1900, 1, 1).toString())
                        .param("second-date", LocalDate.of(2200, 1, 1).toString()))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.[0].id", is(Math.toIntExact(productByTypeOrDateDTO.getId()))))
                        .andExpect(jsonPath("$.[0].footprint", is(productByTypeOrDateDTO.getFootprint())))
                        .andExpect(jsonPath("$.[0].price", is(productByTypeOrDateDTO.getPrice())))
                        .andExpect(jsonPath("$.[0].missionName", is(productByTypeOrDateDTO.getMissionName())));
            }
        }
    }
}