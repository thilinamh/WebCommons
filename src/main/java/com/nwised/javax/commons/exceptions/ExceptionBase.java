package com.nwised.javax.commons.exceptions;

/**
 * Created by thilina_h on 9/15/2017.
 */
public class ExceptionBase extends Exception{

    private int httpCode;
    private String ecode;
    private String message;

    public ExceptionBase(String message, Throwable cause, int httpCode, String ecode) {
        super(message, cause);
        this.httpCode = httpCode;
        this.ecode = ecode;
        this.message = message;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getEcode() {
        return ecode;
    }


    public String getErrorMessage() {
        return message;
    }


}
