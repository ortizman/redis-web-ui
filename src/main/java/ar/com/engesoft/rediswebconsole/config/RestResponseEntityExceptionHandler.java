package ar.com.engesoft.rediswebconsole.config;


import ar.com.engesoft.rediswebconsole.dtos.ErrorDTO;
import ar.com.engesoft.rediswebconsole.exceptions.ForbiddenException;
import ar.com.engesoft.rediswebconsole.exceptions.NotFoundException;
import ar.com.engesoft.rediswebconsole.exceptions.ServiceException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public RestResponseEntityExceptionHandler() {
        super();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler({ServiceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequest(final ServiceException ex, final WebRequest request) {
        final ErrorDTO error = new ErrorDTO(ex);
        log(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleBadRequest(final NotFoundException ex, final WebRequest request) {
        final ErrorDTO resourceNotFound = new ErrorDTO("RESOURCE_NOT_FOUND", ex.getMessage());
        log(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex,
                resourceNotFound, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({TokenExpiredException.class})
    public ResponseEntity<Object> handleTokenException(final TokenExpiredException ex, final WebRequest request) {
        final ErrorDTO tokenExpired = new ErrorDTO("TOKEN_EXPIRED", ex.getMessage());
        log(HttpStatus.UNAUTHORIZED, ex);

        return handleExceptionInternal(ex, tokenExpired, new HttpHeaders(), HttpStatus.UNAUTHORIZED,
                request);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({JWTVerificationException.class})
    public ResponseEntity<Object> handleJWTExceptions(final JWTVerificationException ex, final WebRequest request) {
        final ErrorDTO error = new ErrorDTO(ex);
        log(HttpStatus.UNAUTHORIZED, ex);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.UNAUTHORIZED,
                request);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    public ResponseEntity<Object> handleBadRequest(final AccessDeniedException ex, final WebRequest request) {
        final ErrorDTO accessDenied = new ErrorDTO("ACCESS_DENIED", ex.getMessage());
        log(HttpStatus.FORBIDDEN, ex);

        return handleExceptionInternal(ex, accessDenied, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleBadRequest(final RuntimeException ex, final WebRequest request) {
        final ErrorDTO error = new ErrorDTO(ex);
        log(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationEx(final ConstraintViolationException ex, final WebRequest request) {
        final ErrorDTO errorDTO = new ErrorDTO(ex.getConstraintViolations().toString(), ex.getLocalizedMessage());
        log(HttpStatus.BAD_REQUEST, ex);

        return handleExceptionInternal(ex,
                errorDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(final AuthenticationException ex, final WebRequest request) {
        final ErrorDTO error = new ErrorDTO(ex);
        log(HttpStatus.FORBIDDEN, ex);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        final ErrorDTO error = new ErrorDTO(ex);
        log(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    private void log(HttpStatus status, Throwable t) {
        LOGGER.error("status: {}", status.getReasonPhrase(), t);
    }
}