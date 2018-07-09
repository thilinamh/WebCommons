package com.nwised.javax.commons.jaxrs.webexceptions;

import javax.ws.rs.core.Response;

/**
 * Created by tm on 7/25/17.
 */
public class SomethingWentWrong extends WebExceptionBase {
    private static String ecode = "unknown";
    private static String data = "Oops! Something Went Wrong";
    private static Response.Status http_code = Response.Status.fromStatusCode(500);

    public SomethingWentWrong(String data) {
        super(ecode, data, http_code);
    }

    public SomethingWentWrong(String data, String ecode) {
        super(ecode, data, http_code);
    }

    public SomethingWentWrong(String data, boolean visibility) {
        super(ecode, visibility, data, http_code);
    }

    public SomethingWentWrong(Throwable throwable) {
        super(ecode, false, throwable.getMessage(), http_code);
    }

    public SomethingWentWrong() {
        super(ecode, data, http_code);
    }
}
