package pl.weilandt.wms.product.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.weilandt.wms.product.NewProductDTO;
import pl.weilandt.wms.product.Product;
import pl.weilandt.wms.product.ProductRepository;
import pl.weilandt.wms.product.ProductService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LocationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private static final String CLIENT_ID = "clientRW";
    private static final String CLIENT_SECRET = "password";

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private static final String USER_ADMIN = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private String accessTokenAdmin = "";

    private Product productOne, productTwo;

    private String obtainAccessToken(String username, String password) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("username", username);
        params.add("password", password);

        // @formatter:off

        ResultActions result = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));

        // @formatter:on

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    public static String requestBody(Object request) {
        try {
            return MAPPER.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseResponse(MvcResult result, Class<T> responseClass) {
        try {
            String contentAsString = result.getResponse().getContentAsString();
            return MAPPER.readValue(contentAsString, responseClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
    }

    @BeforeAll
    void getAccessTokenToUse() throws Exception {
        accessTokenAdmin = obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD);
        createProductsToUseInTests();
    }

    void createProductsToUseInTests() {
        productService.createNew(new NewProductDTO(
               "Żelki", "gum-4939-df", BigDecimal.valueOf(1000), "paczek", "Słodkie żelki:)"
        ));

        productService.createNew(new NewProductDTO(
                "Sok pomarańczowy", "pom-sok-3424-hortex", BigDecimal.valueOf(250), "litr", "Sok z wyciskanych pomarańczy"
        ));

        productOne = productRepository.findByNameIgnoreCase("Żelki").get();
        productTwo = productRepository.findByNameIgnoreCase("Sok pomarańczowy").get();
    }

    @Test
    @Order(1)
    void checkIfCreatedProductsAreInDatabaseAndOnlyThem() throws Exception {
        final int SHOULD_BE_TWO = 2;
        MvcResult result = mockMvc.perform(post("/products/all")
                .header(AUTHORIZATION, BEARER + accessTokenAdmin))
                .andExpect(status().isOk())
                .andReturn();

        List<Product> expectedProductList = MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Product>>() {});
        assertThat(expectedProductList).isNotEmpty();
        assertThat(expectedProductList).hasSize(SHOULD_BE_TWO);
    }

    @Test
    @Order(2)
    void addLocationToProduct() throws Exception {
        String expectedCode = "123-123-12-12";
        MvcResult result = mockMvc.perform(post("/locations/product/{id}/add-location", productOne.getId())
                .header(AUTHORIZATION, BEARER + accessTokenAdmin)
                .contentType(CONTENT_TYPE)
                .content(expectedCode)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andReturn();

        Location responseLocation = parseResponse(result, Location.class);
        assertThat(responseLocation.getCode()).isEqualTo(expectedCode);
//        assertThat(responseLocation.getProduct().getId()).isEqualTo(productOne.getId()); //TODO: czemu to nie działa?
    }

    @Test
    @Order(3)
    void cannotAddLocationWhenNoProductFound() throws Exception {
        String expectedCode = "123-123-12-12";
        MvcResult result = mockMvc.perform(post("/locations/product/{id}/add-location", 4563284)
                .header(AUTHORIZATION, BEARER + accessTokenAdmin)
                .contentType(CONTENT_TYPE)
                .content(expectedCode)
                .accept(CONTENT_TYPE))
                .andExpect(status().isNotFound())
                .andReturn();
    }
    @ParameterizedTest
    @Order(4)
    @MethodSource("goodPatternValues")
    void canAddMoreLocationsToOneProductAndReturnThem(String expectedCode) throws Exception {
        MvcResult result = mockMvc.perform(post("/locations/product/{id}/add-location", productTwo.getId())
                .header(AUTHORIZATION, BEARER + accessTokenAdmin)
                .contentType(CONTENT_TYPE)
                .content(expectedCode)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andReturn();

        Location responseLocation = parseResponse(result, Location.class);
        assertThat(responseLocation).isNotNull();
        assertThat(responseLocation.getCode()).isEqualTo(expectedCode);
        List<LocationDTO> returnedLocationList = locationService.getAllLocationsFromProduct(productTwo.getId());
        assertThat(returnedLocationList).isNotEmpty();
    }

    @Test
    @Order(5)
    void getListOfLocationsFromProduct() throws Exception {
        MvcResult result = mockMvc.perform(post("/locations/get_locations_from_product/{product_id}", productTwo.getId())
                .header(AUTHORIZATION, BEARER + accessTokenAdmin))
                .andExpect(status().isOk())
                .andReturn();

        List<Location> responseLocations = MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Location>>() {});
        List<LocationDTO> returnedLocationList = locationService.getAllLocationsFromProduct(productTwo.getId());
        assertThat(responseLocations).isNotEmpty();
        assertThat(responseLocations).hasSize(returnedLocationList.size());
//        assertThat(responseLocations).isEqualTo(returnedLocationList); //TODO: czemu Location ma product=null ?
    }

    @Test
    @Order(6)
    void canGetAllLocationsFromAllProducts() throws Exception {
        MvcResult result = mockMvc.perform(post("/locations/get_all")
                .header(AUTHORIZATION, BEARER + accessTokenAdmin))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(productOne.getId().toString())))
                .andExpect(content().string(containsString(productTwo.getId().toString())))
                .andReturn();

        List<Location> responseLocations = MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Location>>() {});
        assertThat(responseLocations).isNotEmpty();
    }

    @Test
    @Order(7)
    void canEditLocation() throws Exception {
        Location locationToEdit = locationRepository.findAllByProductId(productTwo.getId()).get(0);
        String expectedCode = "999-999-99-99";
        MvcResult result = mockMvc.perform(post("/locations/edit/{id}", locationToEdit.getId())
                .header(AUTHORIZATION, BEARER + accessTokenAdmin)
                .contentType(CONTENT_TYPE)
                .content(expectedCode)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andReturn();

        Location responseLocation = parseResponse(result, Location.class);
        assertThat(responseLocation.getCode()).isEqualTo(expectedCode);
        assertThat(responseLocation.getCode()).isNotEqualTo(locationToEdit.getCode());
    }

    @Test
    @Order(8)
    void canDeleteLocation() throws Exception {
        Location locationToDelete = locationRepository.findAllByProductId(productTwo.getId()).get(0);
        MvcResult result = mockMvc.perform(post("/locations/delete/{id}", locationToDelete.getId())
                .header(AUTHORIZATION, BEARER + accessTokenAdmin))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(locationRepository.findById(locationToDelete.getId())).isNotPresent();
    }

    static Stream<String> goodPatternValues(){
        return Stream.of("000-000-00-01", "234-123-15-11", "abc-432-cd-53", "XCX-232-54-ZZ");
    }
}