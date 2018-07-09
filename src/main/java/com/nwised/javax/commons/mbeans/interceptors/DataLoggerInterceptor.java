package com.nwised.javax.commons.mbeans.interceptors;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.nwised.javax.commons.jaxrs.webexceptions.WebExceptionBase;
import com.nwised.javax.commons.utils.logging.LoggerContext;

import javax.annotation.Priority;
import javax.enterprise.util.Nonbinding;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

/**
 * Created by thilina_h on 10/24/2017.
 */
@Interceptor
@DataLoggerInterceptor.LogData
@Priority(Interceptor.Priority.APPLICATION)
public class DataLoggerInterceptor {

    @InterceptorBinding
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LogData {
        @Nonbinding String[] requestExpressions() default {};

        @Nonbinding String[] responseExpressions() default {};

        @Nonbinding String replaceValue() default "#####";
    }

//    private static final Configuration configuration = Configuration.builder()
//            .jsonProvider(new JacksonJsonNodeJsonProvider())
//            .mappingProvider(new JacksonMappingProvider())
//            .build();


    @Inject
    LoggerContext loggerContext;

    @AroundInvoke
    public Object handleTrans(InvocationContext context) throws Exception {

        Object[] parameters = context.getParameters();
        final LogData annotation = context.getMethod().getAnnotation(LogData.class);
        final String[] respExprs = annotation.responseExpressions();
        final String[] requestExpressions = annotation.requestExpressions();
        final String replaceValue = annotation.replaceValue();

        if (parameters != null && parameters.length > 0) {
            Parameter[] method_params = context.getMethod().getParameters();
            int i = 0;
            StringBuilder builder = new StringBuilder();
            for (Parameter param : method_params) {
//                System.out.println(param.getName() + ":" + parameters[i++]);
                Object reqParameter = parameters[i++];
                if (reqParameter instanceof String) {
                    reqParameter = createLogString(requestExpressions, replaceValue, (String) reqParameter);
                }
                builder.append(param.getName()).append(":").append(reqParameter);
            }
            loggerContext.addElement(LoggerContext.KEYS.REQUEST_BODY.getKey(), builder.toString());
        }
        try {
            Object response = context.proceed();
            if (response instanceof Response) {
                Response restResponse = (Response) response;
                Object entity = restResponse.getEntity();
                if (entity instanceof String) {
                    final String log_msg = createLogString(respExprs, replaceValue, (String) entity);
                    loggerContext.addElement(LoggerContext.KEYS.RESPONSE_BODY.getKey(), log_msg);
                }
            } else if (response instanceof String) {
                final String log_msg = createLogString(respExprs, replaceValue, (String) response);
                loggerContext.addElement(LoggerContext.KEYS.RESPONSE_BODY.getKey(), log_msg);
            }
            return response;
        } catch (WebExceptionBase ex) {
            Response response = ex.getResponse();
            Object entity = response.getEntity();
            String resp_message = "";
            if (entity instanceof String) {
                resp_message = (String) entity;
            }
            loggerContext.addElement(LoggerContext.KEYS.RESPONSE_BODY.getKey(), resp_message);
            throw ex;
        }
    }

    private String createLogString(String[] exprs, String replaceValue, String originalStr) {
        if (exprs.length == 0) {
            return originalStr;
        }
        final DocumentContext documentContext = JsonPath.parse(originalStr);
        Stream.of(exprs).distinct().forEach(expr -> documentContext.set(expr, replaceValue));
        return documentContext.jsonString();
    }

}
