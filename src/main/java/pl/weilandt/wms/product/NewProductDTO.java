package pl.weilandt.wms.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.StringJoiner;

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

    @Override
    public String toString() {
        return new StringJoiner(", ", NewProductDTO.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("quantity=" + quantity)
                .add("unit='" + unit + "'")
                .add("description='" + description + "'")
                .toString();
    }
}
