package com.nwised.javax.commons.controllers;


import com.nwised.javax.commons.jaxrs.webexceptions.SomethingWentWrong;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.validation.constraints.NotNull;


/**
 * Created by tm on 7/25/17.
 */
public interface Controller {
//    Class<? extends Enum<? extends ControllerCommands>> commandClass;

    default <T extends Enum & ControllerCommands> JsonObjectBuilder processRequest(@NotNull T command, JsonObject obj) {
        throw new SomethingWentWrong("Method not yet implemented", false);
    }

    default <T extends Enum & ControllerCommands> JsonObjectBuilder processRequest(@NotNull T command, JsonArray array) {
        throw new SomethingWentWrong("Method not yet implemented", false);
    }

    default <T extends Enum & ControllerCommands> JsonObjectBuilder processRequest(@NotNull T command) {
        throw new SomethingWentWrong("Method not yet implemented", false);
    }
}
