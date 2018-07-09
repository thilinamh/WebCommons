package com.nwised.javax.commons.controllers;

import javax.enterprise.inject.spi.CDI;

/**
 * Created by Thilina on 3/22/2018.
 */
public interface ControllerCommands<T extends Controller> {
    default Controller getController() {
        Class<?> enclosingClass = this.getClass().getEnclosingClass();
        return (T) CDI.current().select(enclosingClass).get();
    }

}
