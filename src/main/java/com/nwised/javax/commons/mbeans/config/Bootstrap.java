package com.nwised.javax.commons.mbeans.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.enterprise.event.Reception.ALWAYS;

/**
 * Created by thilina_h on 9/15/2017.
 */
@WebListener
@ApplicationScoped
public class Bootstrap implements ServletContextListener {

    @Qualifier
    @Retention(RUNTIME)
    @Target({METHOD, PARAMETER, FIELD, TYPE})
    public @interface OnStart {
    }

    @Qualifier
    @Retention(RUNTIME)
    @Target({METHOD, PARAMETER, FIELD, TYPE})
    public @interface OnStop {
    }

    @Inject
    @OnStart
    private Event<LifecycleEvent> evntStart;

    @Inject
    @OnStop
    private Event<LifecycleEvent> evntStop;


    // When ApplicationScoped is initialized this get executed
    public void processApplicationScopedInit(@Observes @Initialized(ApplicationScoped.class) ServletContext payload) {
        System.out.println("*********** Initializing App ***********");
        evntStart.fire(new LifecycleEvent(payload));

    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        evntStop.fire(new LifecycleEvent(servletContextEvent.getServletContext()));
    }

    //@Observes(notifyObserver=ALWAYS)
    public static class LifecycleEvent {
        private ServletContext context;

        public LifecycleEvent(ServletContext context) {
            this.context = context;
        }

        public ServletContext getContext() {
            return context;
        }
    }

    public static abstract class LifeCycleListener {
        //If you have the following args signature in a class method,
        // that class will be automatically instantiated by CDI container
        public synchronized void onStart(@Observes(notifyObserver = ALWAYS) @Bootstrap.OnStart Bootstrap.LifecycleEvent evnt) {
            System.out.println("Loading " + this.getClass().getName());
        }

        public synchronized void onStop(@Observes(notifyObserver = ALWAYS) @Bootstrap.OnStop Bootstrap.LifecycleEvent evnt) {
            System.out.println("Executing " + this.getClass().getName() + " on Stop");
        }
    }

    public interface StartupListener {

        public default void onStart(@Observes(notifyObserver = ALWAYS) @Bootstrap.OnStart Bootstrap.LifecycleEvent evnt) {
            this.onStart(evnt.getContext());
        }

        public void onStart(ServletContext servletContext);
    }


}
