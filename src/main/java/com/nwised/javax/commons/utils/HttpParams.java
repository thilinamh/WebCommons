package com.nwised.javax.commons.utils;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by thilina_h on 4/27/2018.
 */
public class  HttpParams {


    @Inject
    private HttpServletRequest httpReq;

    @Produces
    @HttpParam("")
    String getParamValue(InjectionPoint ip) {

        return httpReq.getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());

    }
}
