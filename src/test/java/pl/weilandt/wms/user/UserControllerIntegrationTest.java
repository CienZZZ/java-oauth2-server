package pl.weilandt.wms.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.weilandt.wms.user.role.Role;
import pl.weilandt.wms.user.role.RoleType;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

//    @Mock
//    private UserDTO userDTO;

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

    private static final int SHOULD_BE_TWO_USERS = 2;

    private static final String NEW_USER_JSON = "{  \"name\": \"Krzys\",  \"password\": \"1234\",  \"registerDate\": \"\",  \"active\": true," +
            "  \"changedPassword\": false,  \"dateLastChange\": \"\",  \"roles\": [    {      \"createdOn\": \"\"," +
            "      \"description\": \"user\",      \"id\": 2,      \"modifiedOn\": \"\",      \"name\": \"USER\"    }  ]}";



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
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
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
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD))
                .contentType(CONTENT_TYPE)
                .content(NEW_USER_JSON)
                .accept(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(userService.getUserByName(USER_NEWUSER).getName())));
    }

    @Test
    @Order(4)
    void accessHaveOnlyAdmin() throws Exception {
        mockMvc.perform(post("/users/all")
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/all")
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_NEWUSER, NEWUSER_PASSWORD)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    void getUserByName() throws Exception {
        mockMvc.perform(post("/users/name/{name}", USER_NEWUSER)
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isFound())
                .andExpect(content().string(containsString(userService.getUserByName(USER_NEWUSER).getName())));
    }

    @Test
    @Order(6)
    void getUserByNameAndCheckParseResponse() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/name/{name}", USER_NEWUSER)
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isFound())
                .andReturn();

        User user = parseResponse(result, User.class);
        assertThat(user.getName()).isEqualTo(USER_NEWUSER);
    }

    @Test
    @Order(7)
    void returnCorrectListOfUsers() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/all")
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();

        List<User> userList = MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<List<User>>() {});
        assertThat(userList).hasSize(SHOULD_BE_TWO_USERS);
        assertThat(userList).extracting(User::getName).containsExactly(USER_ADMIN, USER_NEWUSER);
    }

    @Test
    @Order(8)
    void userRoleIsCorrect() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/all")
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();

        List<User> userList = MAPPER.readValue(result.getResponse().getContentAsString(), new TypeReference<List<User>>() {});
        assertThat(userList.get(0).getRoles()).extracting(Role::getName).contains(RoleType.ADMIN, RoleType.USER);
        assertThat(userList.get(1).getRoles()).extracting(Role::getName).contains(RoleType.USER);
    }

    @Test
    @Order(9)
    void getCorrectUserById() throws Exception {
        UserDTO userDTO = userService.getUserByName(USER_NEWUSER);
        MvcResult result = mockMvc.perform(post("/users/{id}", userDTO.getId())
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();

        User userResponse = parseResponse(result, User.class);
        userResponse.toUserDTO();
        assertThat(userResponse.getId()).isEqualTo(userDTO.getId());
        assertThat(userResponse.getName()).isEqualTo(userDTO.getName());
        assertThat(userResponse.getPassword()).isEqualTo(userDTO.getPassword());
        assertThat(userResponse.getRegisterDate()).isEqualTo(userDTO.getRegisterDate());
        assertThat(userResponse.getActive()).isEqualTo(userDTO.getActive());
        assertThat(userResponse.getChangedPassword()).isEqualTo(userDTO.getChangedPassword());
        assertThat(userResponse.getDateLastChange()).isEqualTo(userDTO.getDateLastChange());
        assertThat(userResponse.getRoles().size()).isEqualTo(userDTO.getRoles().size());
        assertThat(userResponse.getRoles().containsAll(userDTO.getRoles()));
    }

    @Test
    @Order(10)
    void passwordChanged() throws Exception {
        UserDTO userDTO = userService.getUserByName(USER_NEWUSER);
        String newPassword= "5678ws";
        MvcResult result = mockMvc.perform(post("/users/{id}/password/{password}", userDTO.getId(), newPassword)
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();

        User userResponse = parseResponse(result, User.class);
        UserDTO userDTOafterChange = userService.getUserByName(USER_NEWUSER);
        assertThat(userResponse.getPassword()).isEqualTo(userDTOafterChange.getPassword());
    }

    @Test
    @Order(11)
    void userDeleted() throws Exception {
        UserDTO userDTO = userService.getUserByName(USER_NEWUSER);
        MvcResult result = mockMvc.perform(post("/users/delete/{id}", userDTO.getId())
                .header(AUTHORIZATION, BEARER + obtainAccessToken(USER_ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(userRepository.findByNameIgnoreCase(USER_NEWUSER)).isNotPresent();
    }
}