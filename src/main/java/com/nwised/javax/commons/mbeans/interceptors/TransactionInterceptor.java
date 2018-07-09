package com.nwised.javax.commons.mbeans.interceptors;


import com.nwised.javax.commons.jaxrs.webexceptions.OperationRequest;
import com.nwised.javax.commons.db.DBHandler;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.InvocationContext;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by thilina_h on 9/19/2017.
 */

@Interceptor
@TransactionInterceptor.Transactional
@Priority(Interceptor.Priority.APPLICATION)
public class TransactionInterceptor {
    @InterceptorBinding
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Transactional {

    }

    @Inject
    DBHandler.OnDemandConnection onDemandConnection;

    org.slf4j.Logger debugLogger = LoggerFactory.getLogger(TransactionInterceptor.class);

    @Inject
    TransactionalInterceptorSatate interceptorSatate;

    @AroundInvoke
    public Object handleTransaction(InvocationContext ictx) throws Exception {
        if (interceptorSatate.isCalledBefore()) {
            return ictx.proceed();
        } else {
            interceptorSatate.addClass(ictx.getMethod().getDeclaringClass());
            debugLogger.info(TransactionInterceptor.class.getSimpleName() + " called  on :" + ictx.getMethod().getDeclaringClass().getSimpleName() + " " + ictx.getMethod().getName());
            onDemandConnection.beginTransaction();
            try {
                Object proceed = ictx.proceed();
                debugLogger.info("Ending Transaction");
                onDemandConnection.endTransaction();
                return proceed;
            } catch (OperationRequest operationRequest) {//If OperationRequest exception is thrown, commit the trans
                onDemandConnection.endTransaction();
                throw operationRequest;
            }

        }

    }

    @RequestScoped
    static class TransactionalInterceptorSatate {
        private Set<Class<?>> called_class_list = new HashSet<>();

        public void addClass(Class<?> cls) {
            called_class_list.add(cls);
        }

        public boolean isCalledBefore() {
            return called_class_list.size() > 0;
        }

        public boolean hasCalledOn(Class<?> cls) {
            return called_class_list.contains(cls);
        }
    }
}
