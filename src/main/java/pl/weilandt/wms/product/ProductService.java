package pl.weilandt.wms.product;

import io.vavr.collection.List;

import java.util.Optional;

public interface ProductService {

    List<ProductDTO> getAll();
    ProductDTO getOne(long id);
    Optional<ProductDTO> edit(ProductDTO productDTO);
    ProductDTO createNew(NewProductDTO newProductDTO);
    void delete(long id);
}
