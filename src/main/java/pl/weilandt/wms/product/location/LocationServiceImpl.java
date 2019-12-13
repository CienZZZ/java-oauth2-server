package pl.weilandt.wms.product.location;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.weilandt.wms.exception.NoProductException;
import pl.weilandt.wms.product.Product;
import pl.weilandt.wms.product.ProductDTO;
import pl.weilandt.wms.product.ProductRepository;

import java.util.Optional;

@Transactional
@Service("locationService")
public class LocationServiceImpl implements LocationService {

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;

    public LocationServiceImpl(ProductRepository productRepository, LocationRepository locationRepository) {
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public LocationDTO addLocation(long productId, String code) {
        final Optional<Product> product = this.productRepository.findById(productId);
        return product.map( p->{
            final Location location = new Location(
                    code,
                    p
            );
            this.locationRepository.save(location);
            return location.toLocationDTO();
        }).orElseThrow(
                ()-> new NoProductException(productId)
        );
    }

    @Override
    public List<Location> getAllLocationsFromProduct(long productId) {

        return null;
    }

    @Override
    public Map<ProductDTO, Location> getAllLocationsFromAllProducts() {

        return null;
    }
}
