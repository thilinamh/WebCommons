package com.nwised.javax.commons.exceptions;

/**
 * Created by thilina_h on 9/15/2017.
 */
public class EmptyResultException extends ExceptionBase {
    private static int httpCode = 304;
    private static String ecode = "No Data Found";

    public EmptyResultException(String message, Throwable cause, int httpCode, String ecode) {
        super(message, cause, httpCode, ecode);
    }
    public EmptyResultException(String message,Throwable cause){
        this(message,cause,httpCode,ecode);
    }
    public EmptyResultException(String message){
        this(message,null,httpCode,ecode);
    }

    public EmptyResultException() {
        this("Data Not Available");
    }
}