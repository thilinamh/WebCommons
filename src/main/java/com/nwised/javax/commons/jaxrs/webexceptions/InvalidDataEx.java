package com.nwised.javax.commons.jaxrs.webexceptions;

import javax.ws.rs.core.Response;

/**
 * Created by thilina_h on 11/27/2017.
 */
public class InvalidDataEx extends WebExceptionBase {
    private static final Response.Status HTTP_CODE =Response.Status.BAD_REQUEST;
    private static final String ecode="client_01";

    public InvalidDataEx(String ecode, String data) {
        super(ecode, data, HTTP_CODE);
    }
    public InvalidDataEx( String data) {
        super(ecode, data, HTTP_CODE);
    }
}
