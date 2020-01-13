package pl.weilandt.wms.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.jupiter.api.*;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    private static final String CLIENT_ID = "clientRW";
    private static final String CLIENT_SECRET = "password";

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private static final String USER_ADMIN = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private String accessTokenAdmin = "";

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
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @BeforeAll
    void getAccessTokenToUse() throws Exception {
        accessTokenAdmin = obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD);
    }

    @Test
    @Order(1)
    void checkAccessTokenIfWorks() throws Exception {
        mockMvc.perform(post("/users/all")
                .header(AUTHORIZATION, BEARER + accessTokenAdmin))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void getEmptyListOfProducts() throws Exception {
        MvcResult result = mockMvc.perform(post("/products/all")
                .header(AUTHORIZATION, BEARER + accessTokenAdmin))
                .andExpect(status().isOk())
                .andReturn();

        List<Product> expectedProductList = MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Product>>() {});
        assertThat(expectedProductList).isEmpty();
    }

    @Test
    @Order(3)
    void canCreateProduct() throws Exception {
        String expectedProductName = "Piwo";
        String expectedProductCode = "piw-321";
        BigDecimal expectedProductQuantity = BigDecimal.valueOf(120);
        String expectedProductUnit = "szt";
        String expectedProductDescription = "Dobre Piwo!";

        NewProductDTO productCreated = new NewProductDTO(expectedProductName, expectedProductCode, expectedProductQuantity, expectedProductUnit, expectedProductDescription);

        MvcResult result = mockMvc.perform(post("/products/new")
                .header(AUTHORIZATION, BEARER + accessTokenAdmin)
                .contentType(CONTENT_TYPE)
                .content(requestBody(productCreated))
                .accept(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andReturn();

        Product responseProduct = parseResponse(result, Product.class);
        assertThat(responseProduct.getName()).isEqualTo(expectedProductName);
        assertThat(responseProduct.getCode()).isEqualTo(expectedProductCode);
        assertThat(responseProduct.getQuantity()).isEqualTo(expectedProductQuantity);
        assertThat(responseProduct.getUnit()).isEqualTo(expectedProductUnit);
        assertThat(responseProduct.getDescription()).isEqualTo(expectedProductDescription);
    }
}