package com.nwised.javax.commons.utils.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Created by thilina_h on 4/23/2018.
 */
class LogFactory {


    @Produces
    Logger createLogger(InjectionPoint injectionPoint) {

        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());

    }


}