package com.nwised.javax.commons.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.*;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import java.util.logging.Logger;

/**
 * Created by thilina_h on 10/26/2017.
 */
public class CDIUtils {

    //    @Inject
//    Instance<User> userInstance;
//
    @Inject
    BeanManager beanManager;
//
//    public boolean doesBeanExists(Class cls){
//
//        beanManager.getContext(RequestScoped.class).get(beanManager.f);
//    }

    public static <T> void inject(T anObject) {
        BeanManager beanManager = CDI.current().getBeanManager();

        AnnotatedType<?> at = beanManager.createAnnotatedType(anObject.getClass());
        InjectionTarget<T> it = (InjectionTarget<T>) beanManager.createInjectionTarget(at);

        CreationalContext<T> creationalContext = beanManager.createCreationalContext(null);

        // Perform inject
        it.inject(anObject, creationalContext);


//        Unmanaged<Foo> unmanagedFoo = new Unmanaged<Foo>(Foo.class);
//        UnmanagedInstance<Foo> fooInstance = unmanagedFoo.newInstance();
//        Foo foo = fooInstance.produce().inject().postConstruct().get();
//        // Use the foo instance
//        fooInstance.preDestroy().dispose();
    }

    public boolean isRequestScopedActive() {
        try {
            return beanManager.getContext(RequestScoped.class).isActive();
        } catch (ContextNotActiveException e) {
            return false;
        }
    }

    public boolean isApplicationScopedActive() {
        return beanManager.getContext(ApplicationScoped.class).isActive();
    }

    //    @Produces //TODO-use For logging
//    public Device produce(InjectionPoint point){
//        point.getMember().getDeclaringClass();
//    }
    @Produces
    Logger createLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}
