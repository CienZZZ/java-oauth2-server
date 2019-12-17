package pl.weilandt.wms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotValidPattern extends RuntimeException {
    public NotValidPattern(String pattern){super("Pattern not match: " + pattern);}
}
