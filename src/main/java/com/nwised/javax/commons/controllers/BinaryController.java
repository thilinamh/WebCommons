package com.nwised.javax.commons.controllers;


import com.nwised.javax.commons.jaxrs.webexceptions.SomethingWentWrong;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Created by thilina_h on 3/28/2018.
 */
public interface BinaryController extends Controller {

    default <T extends Enum & ControllerCommands> Response.ResponseBuilder processBinaryRequest(@NotNull T command, JsonObject obj) {
        throw new SomethingWentWrong("Method not yet implemented", false);
    }

    default <T extends Enum & ControllerCommands> Response.ResponseBuilder processBinaryRequest(@NotNull T command, JsonArray array) {
        throw new SomethingWentWrong("Method not yet implemented", false);
    }

    default <T extends Enum & ControllerCommands> JsonObjectBuilder processBinaryRequest(@NotNull T command, InputStream stream) {
        throw new SomethingWentWrong("Method not yet implemented", false);
    }

    default <T extends Enum & ControllerCommands> Response.ResponseBuilder processBinaryRequest(@NotNull T command) {
        throw new SomethingWentWrong("Method not yet implemented", false);
    }
}
