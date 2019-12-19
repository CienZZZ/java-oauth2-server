package pl.weilandt.wms.user;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.weilandt.wms.WmsApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest(classes = WmsApplication.class)
@AutoConfigureMockMvc
public class LoginMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @InjectMocks
    UserController userController;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    @WithMockUser
    void unauthorized() throws Exception {
        mockMvc.perform(post("/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
    }

//    @Test
//    @WithMockUser
//    public void shouldAllowUserWithUserRole() throws Exception {
////        mockMvc.perform(MockMvcRequestBuilders.get("/oauth/token")
////        .accept(MediaType.ALL))
////                .andExpect(status().isOk());
//       // mockMvc.perform(get("/oauth/token"));
//
////        mockMvc.perform(post("/users/new")
////                .with(httpBasic("clientRW", "password"))
////                .with(user("user").password("pass").roles("USER"))
////                .accept(MediaType.APPLICATION_JSON_VALUE))
////                .andExpect(status().isCreated());
////        mockMvc.perform(post("/oauth/token")
////                .with(httpBasic("clientRW", "password"))
////                .with(user("user").password("pass").roles("USER"))
////                .accept(MediaType.APPLICATION_JSON_VALUE))
////                .andExpect(status().isAccepted());
//        mockMvc.perform(formLogin("/oauth/token").user("clientRW").password("password"));
//    }


}
