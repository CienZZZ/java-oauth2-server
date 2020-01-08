package pl.weilandt.wms.user;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTestIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserDTO userDTO;

    @InjectMocks
    private UserController userController;

    private static final String CLIENT_ID = "clientRW";
    private static final String CLIENT_SECRET = "password";

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

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
    void unauthorized() throws Exception {
        mockMvc.perform(post("/users/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAuthorization() throws Exception {
        final String accessToken = obtainAccessToken("admin", "admin");
        mockMvc.perform(post("/users/all")
                .header("Authorization", "Bearer" + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    void newUserCreated() throws Exception {
        final String accessToken = obtainAccessToken("admin", "admin");

        String newUser = "{  \"name\": \"Krzys\",  \"password\": \"1234\",  \"registerDate\": \"\",  \"active\": true," +
                "  \"changedPassword\": false,  \"dateLastChange\": \"\",  \"roles\": [    {      \"createdOn\": \"\"," +
                "      \"description\": \"user\",      \"id\": 2,      \"modifiedOn\": \"\",      \"name\": \"USER\"    }  ]}";

        mockMvc.perform(post("/users/new")
                .header("Authorization", "Bearer"+ accessToken)
                .contentType(CONTENT_TYPE)
                .content(newUser)
                .accept(CONTENT_TYPE))
                .andExpect(status().isCreated());
    }

    @Test
    void accessHaveOnlyAdmin() throws Exception {
        final String accessToken = obtainAccessToken("admin", "admin");

        mockMvc.perform(post("/users/all")
                .header("Authorization", "Bearer" + accessToken))
                .andExpect(status().isOk());

        final String accessTokenUser = obtainAccessToken("Krzys", "1234");

        mockMvc.perform(post("/users/all")
                .header("Authorization", "Bearer" + accessTokenUser))
                .andExpect(status().isForbidden());
    }

//    @Test             // nie działa z @WithMockUser    error: bad credentials
//    @WithMockUser(value = "Adam", username = "Adam", password = "4321", roles = {"USER"}, authorities = {})
//    @WithMockUser(value = "Adam", username = "Adam", password = "$2y$12$8DfbAeJQJwtTMeO48wsFQuUUKctKeAcd08aJXWjg4u9PrZxp2hG3G", roles = {"USER"}, authorities = {})
//    void accessHaveOnlyAdmin2() throws Exception {
//        final String accessToken = obtainAccessToken("Adam", "4321");
//        mockMvc.perform(post("/users/all")
//                .header("Authorization", "Bearer" + accessToken))
//                .andExpect(status().isOk());
//    }
}