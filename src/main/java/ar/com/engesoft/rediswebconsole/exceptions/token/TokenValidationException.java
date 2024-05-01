package ar.com.engesoft.rediswebconsole.exceptions.token;

public class TokenValidationException extends BaseTokenException {

    private static final long serialVersionUID = -2586557186768772381L;

    public TokenValidationException(Exception e) {
        super(e);
    }

}