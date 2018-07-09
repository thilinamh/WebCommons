package com.nwised.javax.commons.logging.logback;

import org.slf4j.MDC;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import java.util.UUID;

/**
 * Created by thilina_h on 2/22/2018.
 */
@RequestScoped
public class MDCConfig {

    String session = UUID.randomUUID().toString();

    //    @PostConstruct
    public void init() {
        MDC.put("req_id", session);
    }

    public void setSession(String sessionSHA) {
        MDC.put("session", sessionSHA);
    }

    public void setUserName(String uname) {
        MDC.put("uname", uname);
    }

    @PreDestroy
    public void reset() {
        MDC.clear();
    }
}
