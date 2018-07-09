package com.nwised.javax.commons.controllers;


import com.nwised.javax.commons.jaxrs.webexceptions.InvalidDataEx;
import com.nwised.javax.commons.jaxrs.webexceptions.SomethingWentWrong;

import javax.enterprise.context.RequestScoped;
import javax.inject.Qualifier;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Created by thilina_h on 3/28/2018.
 */
@RequestScoped
@BinaryRequestMediator.Binary
public class BinaryRequestMediator implements RequestMediator {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD, FIELD, PARAMETER})
    public @interface Binary {

    }

    protected Response.Status httpCode = Response.Status.OK;
    protected String return_status = "ok";

    @Override
    public <T extends Enum & ControllerCommands> Response handleRequest(JsonValue jsonValue, ControllerCommands command) {
        if (command.getController() instanceof BinaryController) {
            BinaryController controller = (BinaryController) command.getController();
            if (jsonValue == JsonValue.NULL || jsonValue == null) {
                return controller.processBinaryRequest((T) command).build();//Get
            } else if (jsonValue.getValueType() == JsonValue.ValueType.OBJECT) {
                return controller.processBinaryRequest((T) command, (JsonObject) jsonValue).build();
            } else if (jsonValue.getValueType() == JsonValue.ValueType.ARRAY) {
                return controller.processBinaryRequest((T) command, (JsonArray) jsonValue).build();
            } else {
                throw new InvalidDataEx("Unsupported data type");
            }
        } else {
            throw new SomethingWentWrong("Invalid Data");
        }
    }

    public <T extends Enum & ControllerCommands> Response handleRequest(InputStream inputStream, ControllerCommands command) {

        if (command.getController() instanceof BinaryController) {
            BinaryController controller = (BinaryController) command.getController();
            if (inputStream == null) {
                return controller.processBinaryRequest((T) command).build();//Get
            } else {
                JsonObjectBuilder jsonObjectBuilder;
                jsonObjectBuilder = controller.processBinaryRequest((T) command, inputStream);
                jsonObjectBuilder.add("status", getReturn_status());
                return Response.status(getHttpCode()).entity(jsonObjectBuilder.build().toString()).build();
            }
        } else {
            throw new SomethingWentWrong("Invalid Data");
        }
    }

    @Override
    public <T extends Enum & ControllerCommands> Response handleRequest(ControllerCommands command) {

        return handleRequest(JsonValue.NULL, command);
    }

    @Override
    public Response.Status getHttpCode() {
        return httpCode;
    }

    @Override
    public void setHttpCode(Response.Status httpCode) {

    }

    @Override
    public String getReturn_status() {
        return return_status;
    }

    @Override
    public void setReturn_status(String return_status) {

    }


}
