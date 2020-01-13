package pl.weilandt.wms.product;

import lombok.Getter;
import lombok.Setter;
import pl.weilandt.wms.product.location.Location;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column( name="id", nullable=false )
    private Long id;

    @Column( name="name", nullable=false, length=255 )
    private String name;

    @Column( name="code" )
    private String code;

    @Column( name="quantity" )
    private BigDecimal quantity;

    @Column( name="unit" )
    private String unit;

    @Column( name="description" )
    private String description;

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private Set<Location> locations;

    Product( String name, String code, BigDecimal quantity, String unit, String description) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.unit = unit;
        this.description = description;
    }

    protected Product() {
    }

    ProductDTO toProductDTO(){
        return new ProductDTO(
                this.getId(),
                this.getName(),
                this.getCode(),
                this.getQuantity(),
                this.getUnit(),
                this.getDescription()
        );
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("quantity=" + quantity)
                .add("unit='" + unit + "'")
                .add("description='" + description + "'")
                .add("locations=" + locations)
                .toString();
    }
}
