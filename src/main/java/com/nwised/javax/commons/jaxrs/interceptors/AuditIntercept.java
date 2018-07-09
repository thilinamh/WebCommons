package com.nwised.javax.commons.jaxrs.interceptors;


import com.nwised.javax.commons.audit.AuditContext;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.InvocationContext;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by thilina_h on 1/30/2018.
 */
@Interceptor
@AuditIntercept.Audited
@Priority(Interceptor.Priority.APPLICATION)
public class AuditIntercept {


    @InterceptorBinding
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Audited {

    }

    @Inject
    AuditContext auditContext;

    @AroundInvoke
    public Object handleTrans(InvocationContext context) throws Exception {

        auditContext.setAuditEnabled(true);

//        Audited annotation = context.getMethod().getAnnotation(Audited.class);
//        auditContext.setOperation(annotation.operation());
        return context.proceed();
//        throw new SomethingWentWrong("Intercepted");
    }

}
