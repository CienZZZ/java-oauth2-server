package pl.weilandt.wms.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger Log = LoggerFactory.getLogger(ProductController.class);

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    //public static final String ROLE_USER = "ROLE_USER";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/all",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductDTO> getProducts(){
        return this.productService.getAll().asJava();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDTO getProduct(@PathVariable("id") long productId){
        return this.productService.getOne(productId);
    }

    @RequestMapping(value = "/new",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody NewProductDTO newProductDTO){
        return this.productService.createNew(newProductDTO);
    }

    @RequestMapping(value = "/edit",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO editProduct(@RequestBody ProductDTO productDTO){
        return this.productService.edit(productDTO);
    }

    @Secured({ROLE_ADMIN})
    @RequestMapping(value = "/delete/{id}",
            method = RequestMethod.POST)
    public void deleteProduct(@PathVariable("id") long id){
        this.productService.delete(id);
    }
}
