package com.nwised.javax.commons.exceptions;

/**
 * Created by thilina_h on 9/15/2017.
 */
public class DataIntegrityException extends ExceptionBase {
    private static int httpCode = 400;
    private static String ecode = "DIE";//"Data Integrity Error";
    public DataIntegrityException(String message) {
        super(message, new java.sql.SQLIntegrityConstraintViolationException(), httpCode, ecode);
    }
}
