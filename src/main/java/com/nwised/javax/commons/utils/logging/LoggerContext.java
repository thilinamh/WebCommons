package com.nwised.javax.commons.utils.logging;




import com.nwised.javax.commons.logging.logback.MDCConfig;
import com.nwised.javax.commons.logging.logback.TraceLogger;
import com.nwised.javax.commons.utils.CommonUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author thilinamh
 */
@RequestScoped
public class LoggerContext {

    public enum KEYS {
        RESPONSE_HEADER("RESPONSE_HEADER"), RESPONSE_HTTP_STATUS("RESPONSE_HTTP_STATUS"),
        REQUEST_HEADER("REQUEST_HEADER"), URL("URL"), RESPONSE_BODY("RESPONSE_BODY"),
        REQUEST_BODY_ORIGINAL("REQUEST_BODY_ORIGINAL"), REQUEST_BODY("REQUEST_BODY");
        private final String key;

        KEYS(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return key;
        }

    }
//    @Inject
//    Logger4j logger4j;

    @Inject
    TraceLogger logger;

    private final List<String> log_elements = new ArrayList<>();

    @Inject
    private HttpServletRequest req;

    @Inject
    MDCConfig mdc;

    @Context
    HttpHeaders headers;


    private LocalTime start = LocalTime.now();

    public void addElement(String key, String message) {
        log_elements.add(key + ":" + message);
    }

    public void addElement(String key, Number message) {
        log_elements.add(key + ":" + message.toString());
    }

    @PostConstruct
    public void initHeadersAndPath() {
        String token = headers.getHeaderString("token");
        if(token !=null){
            mdc.setSession(CommonUtils.sha256Hex(token));
        }
        String path = req.getRequestURI();
        this.addElement(LoggerContext.KEYS.URL.toString(), path);
        Enumeration<String> headerNames = req.getHeaderNames();
        StringBuilder builder = new StringBuilder();
        builder.append(System.lineSeparator()).append("=================================").append(System.lineSeparator());
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            String headerVal = req.getHeader(header).toLowerCase();
            builder.append(header).append(":").append(headerVal).append(System.lineSeparator());
        }
        builder.append("=================================").append(System.lineSeparator());
        this.addElement(LoggerContext.KEYS.REQUEST_HEADER.toString(), builder.toString());

//        MultivaluedMap<String,String> headers=httpHeaders.getRequestHeaders();
//        StringBuilder builder= new StringBuilder();
//        builder.append(System.lineSeparator()).append("=================================").append(System.lineSeparator());
//        headers.entrySet().stream().forEach((entry) -> {
//            builder.append(entry.getKey()).append(":").append(entry.getValue().toString()).append(System.lineSeparator());
//        });
//        builder.append("=================================").append(System.lineSeparator());
//        this.addElement(LoggerContext.KEYS.REQUEST_HEADER.toString(), builder.toString());


    }

    public String getLogMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append(System.lineSeparator()).append("____________________________________________________________        ").append(System.lineSeparator());

        builder.append(System.lineSeparator());
        log_elements.stream().forEach((element) -> {
            builder.append(System.lineSeparator()).append(element).append(System.lineSeparator());
        });
        builder.append(System.lineSeparator());
        builder.append("____________________________________________________________        ");

        return builder.toString();

    }


    @PreDestroy
    private void doTheLog() {

        this.addElement("Latency in millis ", CommonUtils.unitsBetween(start, LocalTime.now(), ChronoUnit.MILLIS));
        String msg = this.getLogMessage();
        logger.trace(msg);
    }
}
