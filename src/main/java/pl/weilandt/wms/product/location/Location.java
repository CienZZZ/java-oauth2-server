package pl.weilandt.wms.product.location;

import lombok.Getter;
import lombok.Setter;
import pl.weilandt.wms.product.Product;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product_locations")
@Getter
@Setter
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column( name="code", nullable=false, length=255 )
    private String code;

    @ManyToOne
    private Product product;

    public Location(String code, Product product) {
        this.code = code;
        this.product = product;
    }

    protected Location() {
    }


}
