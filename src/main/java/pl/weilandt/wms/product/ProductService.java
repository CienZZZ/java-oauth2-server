package pl.weilandt.wms.product;

import io.vavr.collection.List;

public interface ProductService {

    List<ProductDTO> getAll();
    ProductDTO getOne(long productId);
    ProductDTO edit(ProductDTO productDTO);
    ProductDTO createNew(NewProductDTO newProductDTO);
    void delete(long productId);
}
