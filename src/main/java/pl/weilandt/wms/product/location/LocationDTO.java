package pl.weilandt.wms.product.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationDTO {

    public final String code;

    @JsonCreator
    public LocationDTO(
            @JsonProperty("code") String code) {
        this.code = code;
    }
}
