package pl.weilandt.wms.product.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT * FROM product_locations where product_id = :product_id", nativeQuery = true)
    List<Location> findAllByProductId(@Param("product_id") long product_id);
}
