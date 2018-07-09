package com.nwised.javax.commons.jaxrs.interceptors;

import com.nwised.javax.commons.jaxrs.webexceptions.SomethingWentWrong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by thilina_h on 4/19/2018.
 */
public class ErrorMapper implements ExceptionMapper<RuntimeException>{
    Logger debugLogger= LoggerFactory.getLogger(ErrorMapper.class);

    @Override
    public Response toResponse(RuntimeException e) {
        e.printStackTrace();
        debugLogger.error(e.getMessage(),e);
        return new SomethingWentWrong(e).getResponse();
    }
}
