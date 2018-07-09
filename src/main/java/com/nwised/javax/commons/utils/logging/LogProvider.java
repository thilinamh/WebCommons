package com.nwised.javax.commons.utils.logging;



import com.nwised.javax.commons.logging.logback.LogBackConfigProvider;

import java.io.InputStream;

/**
 * Created by thilina_h on 11/1/2017.
 */

public class LogProvider  implements LogBackConfigProvider {


    @Override
    public String getAppName() {
        return "ServiceApp";
    }

    @Override
    public InputStream getLogbackConfigFile() {
//        return Thread.currentThread().getContextClassLoader().getResourceAsStream("logback_lazyload.xml");
        return null;
    }


    @Override
    public String getRootFolder() {
        return "logs";
    }
}