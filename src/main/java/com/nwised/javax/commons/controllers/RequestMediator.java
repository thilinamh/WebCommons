package com.nwised.javax.commons.controllers;

import javax.json.JsonValue;
import javax.ws.rs.core.Response;

/**
 * Created by thilina_h on 3/28/2018.
 */
public interface RequestMediator {
    <T extends Enum & ControllerCommands> Response handleRequest(JsonValue jsonValue, ControllerCommands command);

    <T extends Enum & ControllerCommands> Response handleRequest(ControllerCommands command);

    Response.Status getHttpCode();

    void setHttpCode(Response.Status httpCode);

    String getReturn_status();

    void setReturn_status(String return_status);
}
