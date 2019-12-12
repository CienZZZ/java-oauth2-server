package pl.weilandt.wms.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.weilandt.wms.location.Location;

import java.math.BigDecimal;
import java.util.Set;

@Getter
public class NewProductDTO {

    public final String name;
    public final String code;
    public final BigDecimal quantity;
    public final String unit;
    public final String description;
    public final Set<Location> locations;

    @JsonCreator
    public NewProductDTO(
            @JsonProperty("name")
                    String name,
            @JsonProperty("code")
                    String code,
            @JsonProperty("quantity")
                    BigDecimal quantity,
            @JsonProperty("unit")
                    String unit,
            @JsonProperty("description")
                    String description,
            @JsonProperty("locations")
                    Set<Location> locations) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.unit = unit;
        this.description = description;
        this.locations = locations;
    }
}
