package pl.weilandt.wms.product;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

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
}
