package com.nwised.javax.commons.logging.logback;

import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by thilina_h on 2/20/2018.
 */
@WebListener
public class LogBackInitializer implements ServletContextListener {


    @Inject
    LogBackConfigProvider logConfigProvider;

    public void init(ServletContext serv_context) {

        String loggingFolder = logConfigProvider.getLogRoot();
        Logger.getLogger(LogBackInitializer.class.getCanonicalName()).log(Level.INFO, "###### Setting Log Home:{0}######", loggingFolder);
        System.setProperty("log.home", loggingFolder);
        System.setProperty("logback.ContextSelector", "JNDI");
        // assume SLF4J is bound to logback in the current environment
        ch.qos.logback.classic.LoggerContext context = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();

        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        // Call context.reset() to clear any previous configuration, e.g. default
        // configuration. For multi-step configuration, omit calling context.reset().
        context.reset();
//        if(logConfigProvider.getLogbackConfigFile()!=null)

        try (InputStream configProvided = logConfigProvider.getLogbackConfigFile()) {
            InputStream congigDefault = Thread.currentThread().getContextClassLoader().getResourceAsStream("logback_lazyload.xml");
            InputStream temp_stream = (configProvided == null ? congigDefault : configProvided);
            configManually(configurator, temp_stream);
            if (temp_stream != null) {
                temp_stream.close();
            }
        } catch (IOException e) {
            Logger.getLogger(LogBackInitializer.class.getCanonicalName()).log(Level.WARNING,
                    "Failed opening provided logback config file. Falling back to default");
            configManually(configurator, serv_context.getResourceAsStream("logback_lazyload.xml"));
        }
        StatusPrinter.print(context);
    }

    private void configManually(JoranConfigurator configurator, InputStream logback_config) {
        try {
            configurator.doConfigure(logback_config);
        } catch (JoranException e) {
            e.printStackTrace();
        }
    }


//    public static void setLogHome(String root_dir, String appname) {
//        String loggingFolder = getLogHome(root_dir, appname);
//        Logger.getLogger(LogBackInitializer.class.getCanonicalName()).log(Level.INFO, "###### Setting Log Home:{0}######", loggingFolder);
//        System.setProperty("log.home", loggingFolder);
//    }
//
//    public static String getLogHome(String root_dir, String appname) {
//        String currentUsersHomeDir = System.getProperty("user.home");
//        return currentUsersHomeDir + File.separator + root_dir + File.separator + appname;
//    }


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        init(servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
