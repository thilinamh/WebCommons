package com.nwised.javax.commons.jaxrs.webexceptions;

import javax.ws.rs.core.Response;

/**
 * Created by Thilina on 3/25/2018.
 */
public class ForbiddenOperation extends WebExceptionBase {
    private static String ecode = "403";
    private static String message = "You are not authorized to perform this operation";
    private static Response.Status status = Response.Status.FORBIDDEN;

    public ForbiddenOperation(String ecode, String data, Response.Status http_code) {
        super(ecode, data, http_code);
    }

    public ForbiddenOperation(String data, Response.Status http_code) {
        super(ecode, data, http_code);
    }

    public ForbiddenOperation(String data) {
        super(ecode, data, status);
    }

    public ForbiddenOperation() {
        super(ecode, message, status);
    }
}
