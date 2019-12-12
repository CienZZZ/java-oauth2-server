package pl.weilandt.wms.product;

import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.weilandt.wms.exception.NoProductException;
import pl.weilandt.wms.exception.ResourceExistsException;
import pl.weilandt.wms.exception.ResourceNotFoundException;

import java.util.Optional;

@Transactional
@Service("productService")
public class ProductServiceImpl implements ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAll(){
        return List.ofAll(this.productRepository.findAll()).map(Product::toProductDTO);
    }

    @Override
    public ProductDTO getOne(long id){
        Optional<Product> product = this.productRepository.findById(id);
        return product.map(p-> p.toProductDTO()).orElseThrow(
                ()-> new NoProductException(id)
        );
    }

    @Override
    public ProductDTO edit(ProductDTO productDTO){
        final Optional<Product> product = this.productRepository.findById(productDTO.getId());
        return product.map( p -> {
           p.setName(productDTO.getName());
           p.setCode(productDTO.getCode());
           p.setQuantity(productDTO.getQuantity());
           p.setUnit(productDTO.getUnit());
           p.setDescription(productDTO.getDescription());
           p.setLocations(productDTO.getLocations());
            return p.toProductDTO();
        }).orElseThrow(
                ()-> new NoProductException(productDTO.getId())
        );
    }

    @Override
    public ProductDTO createNew(NewProductDTO newProduct){
        if (this.productRepository.findByNameIgnoreCase(newProduct.getName()).isPresent()){
            throw new ResourceExistsException(newProduct.getName());
        } else {
            return this.productRepository.save(new Product(
                    newProduct.name,
                    newProduct.code,
                    newProduct.quantity,
                    newProduct.unit,
                    newProduct.description,
                    newProduct.locations
                    )).toProductDTO();
        }
    }

    @Override
    public void delete(long productId){
        if (!this.productRepository.findById(productId).isPresent()){
            throw new ResourceNotFoundException();
        } else {
            this.productRepository.deleteById(productId);
        }
    }


}
