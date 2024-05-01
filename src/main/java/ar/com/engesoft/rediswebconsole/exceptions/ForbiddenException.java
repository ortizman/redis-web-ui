package ar.com.engesoft.rediswebconsole.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "You do not have access to the resource. This event will be reported")
public class ForbiddenException extends AccessDeniedException {

    public ForbiddenException() {
        this("Access is denied");
    }

    public ForbiddenException(String msg) {
        super(msg);
    }

    public ForbiddenException(String msg, Throwable t) {
        super(msg, t);
    }
}
