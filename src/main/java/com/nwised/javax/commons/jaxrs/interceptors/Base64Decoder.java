package com.nwised.javax.commons.jaxrs.interceptors;

import javax.inject.Singleton;
import javax.ws.rs.NameBinding;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Base64;

/**
 * @author thilinamh
 */
@Provider
@Singleton
@Base64Decoder.DecodeRequest
public class Base64Decoder implements ReaderInterceptor {

    @NameBinding
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DecodeRequest {
    }


    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        context.setInputStream(Base64.getDecoder().wrap(context.getInputStream()));
        return context.proceed();
    }
}
