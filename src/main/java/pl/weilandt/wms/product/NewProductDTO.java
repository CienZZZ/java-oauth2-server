package pl.weilandt.wms.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class NewProductDTO {

    public final String name;
    public final String code;
    public final BigDecimal quantity;
    public final String unit;
    public final String description;

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
                    String description) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.unit = unit;
        this.description = description;
    }
}
