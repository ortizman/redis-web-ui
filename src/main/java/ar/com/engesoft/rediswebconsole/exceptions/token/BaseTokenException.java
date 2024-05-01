package ar.com.engesoft.rediswebconsole.exceptions.token;

public abstract class BaseTokenException extends Exception {

    private static final long serialVersionUID = 1L;

    public BaseTokenException() {

    }

    public BaseTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseTokenException(String message) {
        super(message);
    }

    public BaseTokenException(Throwable cause) {
        super(cause);
    }
}