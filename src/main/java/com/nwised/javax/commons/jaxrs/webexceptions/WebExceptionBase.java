package com.nwised.javax.commons.jaxrs.webexceptions;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tm on 7/23/17.
 */
public abstract class WebExceptionBase extends WebApplicationException {
    private String status;
    private Response.Status httpStatus;
    private String ecode;
    private boolean visible;
    private JsonObject data_json;
    private String data_string;


    public static Response createResponse(String status, String ecode, boolean visible, String data, Response.Status http_code) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder
                .add("status", status)
                .add("ecode", ecode)
                .add("visible", visible)
                .add("data_type", "string")
                .add("data", data);

        Response.ResponseBuilder responseB = Response
                .status(http_code)
                .header("parsable_error", true)//client can parse response to json if this exists
                .type(MediaType.APPLICATION_JSON_TYPE);
        if (http_code.getStatusCode() != 304) {// 304 cannot have a body
            responseB.entity(objectBuilder.build().toString());
        }

        return responseB.build();
    }

    public static Response createResponse(String status, String ecode, JsonObject data, Response.Status http_code) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder
                .add("status", status)
                .add("ecode", ecode)
                .add("visible", false)
                .add("data_type", "json")
                .add("data", data);

        Response.ResponseBuilder responseB = Response
                .status(http_code)
                .header("parsable_error", true)//client can parse response to json if this exists
                .type(MediaType.APPLICATION_JSON_TYPE);
        if (http_code.getStatusCode() != 304) {// 304 cannot have a body
            responseB.entity(objectBuilder.build().toString());
        }

        return responseB.build();
    }

    public WebExceptionBase(String status, String ecode, JsonObject data, Response.Status http_code) {
        super(createResponse(status, ecode, data, http_code));
        this.status = status;
        this.ecode = ecode;
        this.visible = false;
        this.data_json = data;
        this.httpStatus = http_code;

    }

    public WebExceptionBase(String status, String ecode, boolean visible, String data, Response.Status http_code) {
        super(createResponse(status, ecode, visible, data, http_code));
        this.status = status;
        this.ecode = ecode;
        this.visible = true;
        this.data_string = data;
        this.httpStatus = http_code;

    }


    public WebExceptionBase(String ecode, JsonObject data, Response.Status http_code) {
        this("err", ecode, data, http_code);
    }

    public WebExceptionBase(String ecode, String data, Response.Status http_code) {
        this("err", ecode, true, data, http_code);
    }

    public WebExceptionBase(String ecode, boolean visibility, String data, Response.Status http_code) {
        this("err", ecode, visibility, data, http_code);
    }


}
