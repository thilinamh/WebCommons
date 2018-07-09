package com.nwised.javax.commons.jaxrs.webexceptions;

import javax.ws.rs.core.Response;

/**
 * Created by thilina_h on 9/18/2017.
 */
public class InvalidCredentialsEx extends WebExceptionBase {
    private static String ecode="cred";
    private static String data="Invalid Credentials. Please retry";
    private static Response.Status http_code=Response.Status.fromStatusCode(403);

    public InvalidCredentialsEx() {
        super(ecode, data, http_code);
    }
    public InvalidCredentialsEx(String data) {
        super(ecode, data, http_code);
    }
    public InvalidCredentialsEx(int attempts_remaining) {
        super(ecode, "Invalid Credentials. Only "+attempts_remaining+" remaining", http_code);
    }
}
