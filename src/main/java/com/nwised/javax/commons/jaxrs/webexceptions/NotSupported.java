package com.nwised.javax.commons.jaxrs.webexceptions;

import javax.ws.rs.core.Response;

/**
 * Created by tm on 7/26/17.
 */
public class NotSupported extends WebExceptionBase {
    private static String ecode="N/S";
    private static Response.Status httpCode=Response.Status.NOT_IMPLEMENTED;

    public NotSupported(String data) {
        super(ecode, data, httpCode);
    }

    public NotSupported(boolean visibility, String data) {
        super(ecode, visibility, data, httpCode);
    }
}
