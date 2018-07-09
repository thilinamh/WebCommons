package com.nwised.javax.commons.jaxrs.interceptors;

import javax.inject.Singleton;
import javax.ws.rs.NameBinding;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Base64;

/**
 *
 * @author thilinamh
 */
@Provider
@Singleton
@Base64Encoder.EncodeResponse
public class Base64Encoder implements WriterInterceptor{

    @NameBinding
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EncodeResponse {
    }


    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        context.setOutputStream(Base64.getEncoder().wrap(context.getOutputStream()));
        context.proceed();
    }

}
