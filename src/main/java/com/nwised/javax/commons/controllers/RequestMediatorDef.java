package com.nwised.javax.commons.controllers;

import com.nwised.javax.commons.jaxrs.webexceptions.InvalidDataEx;
import com.nwised.javax.commons.jaxrs.webexceptions.SomethingWentWrong;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tm on 7/25/17.
 */
@RequestScoped
@Default
@Alternative
public class RequestMediatorDef implements RequestMediator {
    protected Response.Status httpCode = Response.Status.OK;
    protected String return_status = "ok";

    @Override
    public <T extends Enum & ControllerCommands> Response handleRequest(JsonValue jsonValue, ControllerCommands command) {
        try {
            if (command.getController() instanceof HandledPrior) {
                HandledPrior controller = (HandledPrior) command.getController();
                controller.doBeforeProcessing(jsonValue, command);
            }
//            JsonObjectBuilder jsonObjectBuilder = command.getController().processRequest(session_data.getParamValue("user"), command, data);
            JsonObjectBuilder jsonObjectBuilder;
            if (jsonValue == JsonValue.NULL || jsonValue==null) {
                jsonObjectBuilder = command.getController().processRequest((T) command);//Get
            } else if (jsonValue.getValueType() == JsonValue.ValueType.OBJECT) {
                jsonObjectBuilder = command.getController().processRequest((T) command, (JsonObject) jsonValue);
            } else if (jsonValue.getValueType() == JsonValue.ValueType.ARRAY) {
                jsonObjectBuilder = command.getController().processRequest((T) command, (JsonArray) jsonValue);
            } else {
                throw new InvalidDataEx("Unsupported data type");
            }
            jsonObjectBuilder.add("status", return_status);
            return Response.status(getHttpCode()).entity(jsonObjectBuilder.build().toString()).build();
        } catch (WebApplicationException ex) {
//            if (ex.getResponse().getStatus() != 304) {
//                ex.printStackTrace();
//            }
            throw ex;
        } catch (ConstraintViolationException ex) {
            String err = handleViolation(ex);
            throw new SomethingWentWrong("Error occurred due to invalid arguments. \n" + err, false);
        } catch (RuntimeException ex) { //TODO-use ConstraintViolationException for bean validation exceptions
            Logger.getLogger(RequestMediatorDef.class.getName()).log(Level.WARNING, "Error when mediating request", ex);
            ex.printStackTrace();
//            java.util.logging.Logger.getLogger(RequestMediatorDef.class.getName()).log(Level.SEVERE, "Error when mediating request", ex);
            throw new SomethingWentWrong("oops! Something Went Wrong");
        }
    }

    @NotNull
    protected String handleViolation(ConstraintViolationException ex) {
        StringBuilder errorBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : constraintViolations) {
            errorBuilder.append("* ").append(violation.getMessage()).append("\n");
        }
        String err = errorBuilder.toString();
        Logger.getLogger(RequestMediatorDef.class.getName()).log(Level.WARNING, "Bean validation error. \n" + err, ex);
        return err;
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
        this.httpCode = httpCode;
    }

    @Override
    public String getReturn_status() {
        return return_status;
    }

    @Override
    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }
}
