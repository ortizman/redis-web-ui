package ar.com.engesoft.rediswebconsole.exceptions.token;

public class InexistentTokenException extends BaseTokenException {

    private static final long serialVersionUID = -7951152974795859976L;

    public InexistentTokenException(String message) {
        super(message);
    }

}