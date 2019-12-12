package pl.weilandt.wms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceExistsException extends RuntimeException {

    private static final long serialVersionUID = 3632237324369283635L;

    public ResourceExistsException() {
        super("Resource with name exist");
    }

    public ResourceExistsException(String name) {
        super("Resource with name:"+ name +" exist");
    }
}
