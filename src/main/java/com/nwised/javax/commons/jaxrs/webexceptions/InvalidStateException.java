package com.nwised.javax.commons.jaxrs.webexceptions;

import javax.ws.rs.core.Response;

/**
 * Created by thilina_h on 4/24/2018.
 */
public class InvalidStateException extends WebExceptionBase {

    private static final Response.Status HTTP_CODE = Response.Status.FORBIDDEN;
    private static final String ecode = "invalid_state";
    private static final String data = "Invalid State";

    public InvalidStateException(String ecode, String data) {
        super(ecode, false, data, HTTP_CODE);
    }

    public InvalidStateException(String data) {
        super(ecode, false, data, HTTP_CODE);
    }

    public InvalidStateException(String ecode, boolean visibility, String data) {
        super(ecode, visibility, data, HTTP_CODE);
    }

    public InvalidStateException(boolean visibility, String data) {
        super(ecode, visibility, data, HTTP_CODE);
    }

    public InvalidStateException() {
        super(ecode, false, data, HTTP_CODE);
    }
}
