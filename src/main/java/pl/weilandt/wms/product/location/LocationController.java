package pl.weilandt.wms.product.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final Logger Log = LoggerFactory.getLogger(LocationController.class);

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @RequestMapping(value = "/{id}/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LocationDTO addLocation(@PathVariable("id") long id, @RequestBody String code){
        return this.locationService.addLocation(id, code);
    }
}
