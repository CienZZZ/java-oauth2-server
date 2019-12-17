package pl.weilandt.wms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoProductException extends RuntimeException {
    public NoProductException(long id){super("Product: "+ id + " does not exist!");}
}
