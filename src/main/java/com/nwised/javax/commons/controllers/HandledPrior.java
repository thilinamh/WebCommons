package com.nwised.javax.commons.controllers;

import javax.json.JsonValue;

public interface HandledPrior{

    void doBeforeProcessing(JsonValue jsonValue, ControllerCommands command);
}