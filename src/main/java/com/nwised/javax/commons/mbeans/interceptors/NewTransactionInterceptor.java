package com.nwised.javax.commons.mbeans.interceptors;


import com.nwised.javax.commons.db.DBHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.sql.Connection;

/**
 * Created by thilina_h on 9/19/2017.
 * This interceptor will set a new SQL Connection for the OnDemandConnection.
 * This is a autocommit true by default.
 * When You use onDemand connection it will be executed on a new connection.
 * After execution SQL Connection will be replaced with the older one
 */

@Interceptor
@NewTransactionInterceptor.NewTransaction
@Priority(Interceptor.Priority.APPLICATION)
public class NewTransactionInterceptor {
    @InterceptorBinding
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NewTransaction {
        /**
         * Define if the connection is auto committed or not. True by default
         * @return
         */
        boolean autocommit() default true;
    }

    @Inject
    DBHandler.OnDemandConnection onDemandConnection;
    @Inject
    DBHandler dbHandler;

    Logger debugLogger= LoggerFactory.getLogger(NewTransactionInterceptor.class);

    @AroundInvoke
    public Object handleTrans(InvocationContext context) throws Exception {
        debugLogger.info("Trans on new connection begin");
        NewTransaction annotation = context.getMethod().getAnnotation(NewTransaction.class);
        boolean autocommit = true;
        if (annotation != null) {
            autocommit = annotation.autocommit();
        }
        Connection rawConnection = onDemandConnection.getRawConnection();
        try (Connection tempConnection = dbHandler.getRawConnection()) { //Auto closes Connection
            tempConnection.setAutoCommit(autocommit);
            onDemandConnection.setRawConnection(tempConnection); // Assigns new raw connection to the onDemand Connection
            Object response = context.proceed();// Do the New transaction//TODO- this throws a n exception on Credential check interceptor
            debugLogger.info("Trans on new connection end");
            return response;

        } finally {
            onDemandConnection.setRawConnection(rawConnection);
        }

    }
}
