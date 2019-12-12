package pl.weilandt.wms.location;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "locations")
@Getter
@Setter
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column( name="code", nullable=false, length=255 )
    private String code;

    public Location(String code) {
        this.code = code;
    }

    protected Location() {
    }
}
