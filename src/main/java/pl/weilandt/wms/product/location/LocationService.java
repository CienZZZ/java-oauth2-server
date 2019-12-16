package pl.weilandt.wms.product.location;

import java.util.List;

public interface LocationService {

    LocationDTO addLocation(long productId, String code);
    List<LocationDTO> getAllLocationsFromProduct(long productId);
    List<LocationDTO> getAllLocationsFromAllProducts();
}
