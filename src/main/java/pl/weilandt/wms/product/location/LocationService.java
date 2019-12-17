package pl.weilandt.wms.product.location;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.weilandt.wms.exception.NoLocationException;
import pl.weilandt.wms.exception.NoProductException;
import pl.weilandt.wms.exception.NotValidPattern;
import pl.weilandt.wms.exception.ResourceNotFoundException;
import pl.weilandt.wms.product.Product;
import pl.weilandt.wms.product.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service("locationService")
public class LocationService {

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;

    public LocationService(ProductRepository productRepository, LocationRepository locationRepository) {
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
    }


    public LocationDTO addLocation(long productId, String code) {
        final Optional<Product> product = this.productRepository.findById(productId);
        return product.map( p->{
            if (!code.matches("\\w{3}-\\w{3}-\\w{2}-\\w{2}")){
                throw new NotValidPattern("\\w{3}-\\w{3}-\\w{2}-\\w{2}");
            } else {
                final Location location = new Location(
                        code,
                        p
                );
                this.locationRepository.save(location);
                return location.toLocationDTO();
            }
        }).orElseThrow(
                ()-> new NoProductException(productId)
        );
    }


    public List<LocationDTO> getAllLocationsFromProduct(long productId) {
        if (!this.productRepository.findById(productId).isPresent()){
            throw new ResourceNotFoundException();
        } else {
            final List<Location> locations = this.locationRepository.findAllByProductId(productId);
            final List<LocationDTO> locationsDTO = new ArrayList<>();
            for (Location location : locations) {
                locationsDTO.add(location.toLocationDTO());
            }
            return locationsDTO;
        }
    }


    public List<LocationDTO> getAllLocationsFromAllProducts() {
            final List<Location> locations = this.locationRepository.findAll();
            final List<LocationDTO> locationsDTO = new ArrayList<>();
        for (Location location : locations) {
            locationsDTO.add(location.toLocationDTO());
        }
        return locationsDTO;
    }


    public LocationDTO editLocation(long locationId, String newCode) {
        final Optional<Location> location = this.locationRepository.findById(locationId);
        return location.map( l->{
            if (!newCode.matches("\\w{3}-\\w{3}-\\w{2}-\\w{2}")){
                throw new NotValidPattern("\\w{3}-\\w{3}-\\w{2}-\\w{2}");
            } else {
                l.setCode(newCode);
                return l.toLocationDTO();
            }
        }).orElseThrow(
                ()-> new NoLocationException(locationId)
        );
    }


    public void delete(long locationId) {
        if (!this.locationRepository.findById(locationId).isPresent()){
            throw new ResourceNotFoundException();
        } else {
            this.locationRepository.deleteById(locationId);
        }
    }
}
