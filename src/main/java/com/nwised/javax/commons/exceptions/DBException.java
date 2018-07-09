package com.nwised.javax.commons.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by thilina_h on 9/15/2017.
 */
public class DBException extends ExceptionBase {
    private static int httpCode = 500;
    private static String ecode = "I/E";//"Internal Error";

    public DBException(String message, Throwable cause, int httpCode, String ecode) {
        super(message, cause, httpCode, ecode);
    }

    public DBException(String message, Throwable cause) {
        this(message, cause, httpCode, ecode);
        Logger.getLogger(DBException.class.getName()).log(Level.WARNING, message,cause);
    }
    public DBException(String message) {
        this(message, null, httpCode, ecode);
        Logger.getLogger(DBException.class.getName()).log(Level.WARNING, message);
    }
}