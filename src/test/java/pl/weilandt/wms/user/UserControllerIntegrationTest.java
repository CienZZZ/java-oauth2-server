package pl.weilandt.wms.user;

import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDTO userDTO;

    @InjectMocks
    private UserController userController;

    private static final String CLIENT_ID = "clientRW";
    private static final String CLIENT_SECRET = "password";

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    private static final String USER_ADMIN = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private static final String USER_NEWUSER = "Krzys";
    private static final String NEWUSER_PASSWORD = "1234";

    private static final String NEW_USER_JSON = "{  \"name\": \"Krzys\",  \"password\": \"1234\",  \"registerDate\": \"\",  \"active\": true," +
            "  \"changedPassword\": false,  \"dateLastChange\": \"\",  \"roles\": [    {      \"createdOn\": \"\"," +
            "      \"description\": \"user\",      \"id\": 2,      \"modifiedOn\": \"\",      \"name\": \"USER\"    }  ]}";

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

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

    @Test
    @Order(1)
    void unauthorized() throws Exception {
        mockMvc.perform(post("/users/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(2)
    void getAuthorization() throws Exception {
        mockMvc.perform(post("/users/all")
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void newUserCreated() throws Exception {
        mockMvc.perform(post("/users/new")
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN,ADMIN_PASSWORD))
                .contentType(CONTENT_TYPE)
                .content(NEW_USER_JSON)
                .accept(CONTENT_TYPE))
                .andExpect(status().isCreated())
//                .andExpect(content().string(containsString(USER_NEWUSER)));
                .andExpect(content().string(containsString(userService.getUserByName(USER_NEWUSER).getName())));
//                .andReturn();
//        String responseJson = result.getResponse().getContentAsString();
//        assertThat(responseJson).contains("Krzys");
    }

    @Test
    @Order(4)
    void getUserByName() throws Exception {
        mockMvc.perform(post("/users/name/{name}", USER_NEWUSER)
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN,ADMIN_PASSWORD)))
                .andExpect(status().isFound());
    }

    @Test
    @Order(5)
    void accessHaveOnlyAdmin() throws Exception {
        mockMvc.perform(post("/users/all")
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN,ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/all")
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_NEWUSER,NEWUSER_PASSWORD)))
                .andExpect(status().isForbidden());
    }

}