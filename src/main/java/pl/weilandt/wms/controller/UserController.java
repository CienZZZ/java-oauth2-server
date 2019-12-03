package pl.weilandt.wms.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.weilandt.wms.dto.NewUserDTO;
import pl.weilandt.wms.dto.UserDTO;
import pl.weilandt.wms.service.AuthenticationFacadeService;
import pl.weilandt.wms.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    private final UserService userService;
    private final AuthenticationFacadeService authenticationFacadeService;

    public UserController(UserService userService, AuthenticationFacadeService authenticationFacadeService) {
        this.userService = userService;
        this.authenticationFacadeService = authenticationFacadeService;
    }

    @Secured({ROLE_ADMIN})
    @RequestMapping(value = "/all",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getUsers(){
        log.info(String.format("received request to list users %s", authenticationFacadeService.getAuthentication().getPrincipal()));
        return this.userService.getAllUsers().asJava();
    }

    @Secured({ROLE_ADMIN, ROLE_USER})
    @RequestMapping(value = "/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUser(@PathVariable("id") Long userId){
        log.info(String.format("received request to update user %s", authenticationFacadeService.getAuthentication().getPrincipal()));
        return this.userService.getUserById(userId);
    }

    @Secured({ROLE_ADMIN})
    @RequestMapping(value = "/new",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody NewUserDTO newUser){
        log.info(String.format("received request to create user %s", authenticationFacadeService.getAuthentication().getPrincipal()));
        return this.userService.save(newUser);
    }

}
