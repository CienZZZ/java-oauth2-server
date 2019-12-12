package pl.weilandt.wms.product;

import lombok.Getter;
import lombok.Setter;
import pl.weilandt.wms.location.Location;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Products_Locations",
            joinColumns =  @JoinColumn(name ="PRODUCT_ID"),inverseJoinColumns= @JoinColumn(name="LOCATION_ID"))
    private Set<Location> locations;

    Product( String name, String code, BigDecimal quantity, String unit, String description, Set<Location> locations) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.unit = unit;
        this.description = description;
        this.locations = locations;
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
                this.getDescription(),
                this.getLocations()
        );
    }
}
