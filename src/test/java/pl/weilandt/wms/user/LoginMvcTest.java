package pl.weilandt.wms.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class LoginMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

//    @Before
//    public void setup(){
//        mockMvc = MockMvcBuilders
//                    .webAppContextSetup(context)
//                    .apply(springSecurity())
//                    .build();
//    }

    @Test
    @WithMockUser
    public void shouldAllowUserWithUserRole() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/oauth/token")
//        .accept(MediaType.ALL))
//                .andExpect(status().isOk());
       // mockMvc.perform(get("/oauth/token"));
        mockMvc.perform(post("/users/new")
                .with(httpBasic("clientRW", "password"))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }


}
