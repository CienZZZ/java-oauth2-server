package pl.weilandt.wms.product.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.StringJoiner;

public class LocationDTO {

    public final long id;
    public final String code;
    public final long product_id;

    @JsonCreator
    public LocationDTO(
            @JsonProperty("id") long id,
            @JsonProperty("code") String code,
            @JsonProperty("product_id") long product_id) {
        this.id = id;
        this.code = code;
        this.product_id = product_id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LocationDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("code='" + code + "'")
                .add("product_id=" + product_id)
                .toString();
    }
}
