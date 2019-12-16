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
public class ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    List<ProductDTO> getAll(){
        return List.ofAll(this.productRepository.findAll()).map(Product::toProductDTO);
    }


    ProductDTO getOne(long productId){
        Optional<Product> product = this.productRepository.findById(productId);
        return product.map(p-> p.toProductDTO()).orElseThrow(
                ()-> new NoProductException(productId)
        );
    }


    ProductDTO edit(ProductDTO productDTO){
        final Optional<Product> product = this.productRepository.findById(productDTO.getId());
        return product.map( p -> {
           p.setName(productDTO.getName());
           p.setCode(productDTO.getCode());
           p.setQuantity(productDTO.getQuantity());
           p.setUnit(productDTO.getUnit());
           p.setDescription(productDTO.getDescription());
            return p.toProductDTO();
        }).orElseThrow(
                ()-> new NoProductException(productDTO.getId())
        );
    }


    ProductDTO createNew(NewProductDTO newProduct){
        if (this.productRepository.findByNameIgnoreCase(newProduct.getName()).isPresent()){
            throw new ResourceExistsException(newProduct.getName());
        } else {
            return this.productRepository.save(new Product(
                    newProduct.name,
                    newProduct.code,
                    newProduct.quantity,
                    newProduct.unit,
                    newProduct.description
                    )).toProductDTO();
        }
    }


    void delete(long productId){
        if (!this.productRepository.findById(productId).isPresent()){
            throw new ResourceNotFoundException();
        } else {
            this.productRepository.deleteById(productId);
        }
    }
}
