package ar.com.engesoft.rediswebconsole.exceptions.token;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class InvalidTokenException extends BaseTokenException {

    private static final long serialVersionUID = 8081669237751911306L;

    public InvalidTokenException(JWTVerificationException e) {
        super(e);
    }

}