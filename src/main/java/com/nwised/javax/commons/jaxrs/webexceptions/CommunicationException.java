package com.nwised.javax.commons.jaxrs.webexceptions;

import javax.ws.rs.core.Response;

/**
 * Created by thilina_h on 12/4/2017.
 */
public class CommunicationException extends WebExceptionBase {
    private static final String ecode = "comm_err_1";
    private static final Response.Status http_code = Response.Status.GONE;

    public CommunicationException(boolean visibility, String data) {
        super(ecode, visibility, data, http_code);
    }

    public CommunicationException(boolean visibility, String data, String ecode) {
        super(ecode, visibility, data, http_code);
    }

    public CommunicationException(String data, String ecode) {
        super(ecode, true, data, http_code);
    }
    public CommunicationException(String data) {
        super(ecode, true, data, http_code);
    }

}
