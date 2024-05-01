package ar.com.engesoft.rediswebconsole.dtos;

import ar.com.engesoft.rediswebconsole.exceptions.ServiceException;
import lombok.ToString;

import java.util.Map;

@ToString
public class ErrorDTO {

    private String message;
    private String code;

    public ErrorDTO() {
        // Constructor por defecto
    }

    public ErrorDTO(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ErrorDTO(ServiceException ex) {
        this(ex.getCode(), ex.getMessage());
    }

    public ErrorDTO(Exception ex) {
        this("UNKNOWN_ERROR", ex.getMessage());
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

}