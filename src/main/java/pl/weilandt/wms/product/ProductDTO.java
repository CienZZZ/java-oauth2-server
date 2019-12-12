package pl.weilandt.wms.product;

import lombok.Getter;
import pl.weilandt.wms.location.Location;

import java.math.BigDecimal;
import java.util.Set;

@Getter
public class ProductDTO {

    public final Long id;
    public final String name;
    public final String code;
    public final BigDecimal quantity;
    public final String unit;
    public final String description;
    public final Set<Location> locations;

    ProductDTO(Long id, String name, String code, BigDecimal quantity, String unit, String description, Set<Location> locations) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.unit = unit;
        this.description = description;
        this.locations = locations;
    }
}
