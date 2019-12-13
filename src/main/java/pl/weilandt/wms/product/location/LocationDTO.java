package pl.weilandt.wms.product.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LocationDTO {

    public final String code;
    public final long product_id;

    @JsonCreator
    public LocationDTO(
            @JsonProperty("code") String code,
            @JsonProperty("product_id") long product_id) {
        this.code = code;
        this.product_id = product_id;
    }
}
