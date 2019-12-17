package pl.weilandt.wms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoLocationException extends RuntimeException {
    public NoLocationException(long id){super("Location: " + id + " does not exists!");}
}
