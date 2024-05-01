package ar.com.engesoft.rediswebconsole.exceptions;

/**
 * @author manuel
 */
public class ServiceException extends RuntimeException {

    private String message;
    private String code;

    private static final long serialVersionUID = 1L;

    public ServiceException(String code, String message) {
        super();
        this.message = message;
        this.code = code;
    }

    public ServiceException(String code) {
        super();
        this.code = code;
    }

    /**
     * Constructor por defecto
     */
    public ServiceException() {

    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
