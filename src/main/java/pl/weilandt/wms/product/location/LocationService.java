package pl.weilandt.wms.product.location;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import pl.weilandt.wms.product.ProductDTO;

public interface LocationService {

    LocationDTO addLocation(long productId, String code);
    List<Location> getAllLocationsFromProduct(long productId);
    Map<ProductDTO, Location> getAllLocationsFromAllProducts();
}
