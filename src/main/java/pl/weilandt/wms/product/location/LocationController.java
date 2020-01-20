package pl.weilandt.wms.product.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final Logger Log = LoggerFactory.getLogger(LocationController.class);

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @RequestMapping(value = "/product/{id}/add-location",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LocationDTO addLocation(@PathVariable("id") long id, @RequestBody String code){
        return this.locationService.addLocation(id, code);
    }

    @RequestMapping(value = "/get_locations_from_product/{product_id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LocationDTO> getLocationsFromProduct(@PathVariable("product_id") long product_id){
        return this.locationService.getAllLocationsFromProduct(product_id);
    }

    @RequestMapping(value = "/get_all",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LocationDTO> getAllLocations(){
        return this.locationService.getAllLocationsFromAllProducts();
    }

    @RequestMapping(value = "/edit/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LocationDTO editLocation(@PathVariable("id") long locationId,@RequestBody String newCode){
        return this.locationService.editLocation(locationId, newCode);
    }

    @Secured({ROLE_ADMIN})
    @RequestMapping(value = "/delete/{id}",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void deleteLocation(@PathVariable("id") long id){
        this.locationService.delete(id);
    }
}
