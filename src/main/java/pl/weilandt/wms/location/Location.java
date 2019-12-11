package pl.weilandt.wms.location;

import lombok.Getter;
import lombok.Setter;
import pl.weilandt.wms.product.Product;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "locations")
@Getter
@Setter
public class Location implements Serializable {

//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    @Column(name = "ID")
//    private long id;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_product_id")
    private Product product_id;

    @Column( name="code", nullable=false, length=255 )
    private String code;
}
