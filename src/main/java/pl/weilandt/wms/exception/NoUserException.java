package pl.weilandt.wms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoUserException extends RuntimeException {
    public NoUserException(long id){
        super("User " + id + " does not exist");
    }
}
