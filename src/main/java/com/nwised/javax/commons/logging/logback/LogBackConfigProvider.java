package com.nwised.javax.commons.logging.logback;

import java.io.File;
import java.io.InputStream;

/**
 * Created by thilina_h on 2/7/2018.
 */

public interface LogBackConfigProvider {

    String getAppName();

    InputStream getLogbackConfigFile();

    String getRootFolder();

    default String getLogRoot() {
        String currentUsersHomeDir = System.getProperty("user.home");
        return currentUsersHomeDir + File.separator + getRootFolder() + File.separator + getAppName();
    }

}
