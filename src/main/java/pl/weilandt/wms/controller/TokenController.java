package pl.weilandt.wms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(
        value = {"/oauth"},
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class TokenController {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    private final DefaultTokenServices tokenServices;

    public TokenController(DefaultTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @Resource(name = "tokenStore")
    TokenStore tokenStore;

    @Secured({ROLE_ADMIN})
    @RequestMapping(value = "/tokens",
            method = RequestMethod.POST)        //TODO jest problem z POST, daje 403 Forbiden, GET nie moze byc bo nawet bez logowania pokazuje tokeny!!!
    @ResponseBody
    public List<String> getTokens() {
        List<String> tokenValues = new ArrayList<String>();
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId("clientRW");
        if (tokens!=null){
            for (OAuth2AccessToken token:tokens){
                tokenValues.add(token.getValue());
            }
        }
        return tokenValues;
    }

    @Secured({ROLE_ADMIN, ROLE_USER})       //TODO to kasuje tokeny z bazy, tego zalogowanego uzytkownika, mozna by uzywac przy wylogowywaniu
    @RequestMapping(path = "/revoke",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void revokeToken(Authentication authentication) {
        final String userToken = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        tokenServices.revokeToken(userToken);
    }
}
