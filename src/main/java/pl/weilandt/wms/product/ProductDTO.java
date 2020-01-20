package pl.weilandt.wms.product;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.StringJoiner;

@Getter
public class ProductDTO {

    public final Long id;
    public final String name;
    public final String code;
    public final BigDecimal quantity;
    public final String unit;
    public final String description;

    ProductDTO(Long id, String name, String code, BigDecimal quantity, String unit, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.unit = unit;
        this.description = description;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("quantity=" + quantity)
                .add("unit='" + unit + "'")
                .add("description='" + description + "'")
                .toString();
    }
}
