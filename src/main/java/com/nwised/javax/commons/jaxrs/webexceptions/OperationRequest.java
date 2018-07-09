package com.nwised.javax.commons.jaxrs.webexceptions;

import javax.json.JsonObject;
import javax.ws.rs.core.Response;

/**
 * Created by thilina_h on 11/23/2017.
 */
public class OperationRequest extends WebExceptionBase {
    static final private String status="redirect";
    static final private Response.Status http_code= Response.Status.OK;
    public OperationRequest(String operation, JsonObject data) {
        super(status, operation, data, http_code);
    }
}
