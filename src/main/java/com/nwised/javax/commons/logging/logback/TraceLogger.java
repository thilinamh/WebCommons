package com.nwised.javax.commons.logging.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by thilina_h on 2/22/2018.
 */
@ApplicationScoped
public class TraceLogger {

     Logger logger = LoggerFactory.getLogger("TRACE_LOGGER");

    public void trace(String data){
        logger.trace(data);
    }

}
