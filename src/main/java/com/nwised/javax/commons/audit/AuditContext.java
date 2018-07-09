package com.nwised.javax.commons.audit;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author thilinamh
 */
@RequestScoped
public class AuditContext {

    @Inject
    private HttpServletRequest httpReq;

    public static AuditContext getCurrentContext() {
        Instance<AuditContext> instance = CDI.current().select(AuditContext.class);
        return instance.get();

    }

    public interface AuditHandler {
        void saveAudit(AuditContext auditContext);
    }

    @Inject
    AuditHandler auditHandler;

    private String uname;
    private String device;
    private String ip;
    private String session_id;
    private long uid;
    private String operation;
    private final StringBuilder description_builder = new StringBuilder();
    private boolean auditEnabled;

    public AuditContext() {
    }

    @PostConstruct
    void onCreate() {
        this.ip = httpReq.getRemoteAddr() + ":" + httpReq.getRemotePort();
        this.device = httpReq.getHeader("User-Agent");
//        this.session_id = httpReq.getHeader(HeaderFields.DATA.toString());
    }

    public void setAuditEnabled(boolean auditEnabled) {
        this.auditEnabled = auditEnabled;
    }


    public void saveAudit() {
        if (auditEnabled) {
            auditHandler.saveAudit(this);
        }
    }

    public AuditContext setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public AuditContext addDetail(String property) {
        description_builder.append(property).append(". ");
        return this;
    }

    public String getDevice() {
        return device;
    }

    public AuditContext setDevice(String device) {
        this.device = device;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    //    public AuditContext setIp(String ip) {
//        this.ip = ip;
//        return this;
//    }
    public String getSession_id() {
        return session_id;
    }

    public AuditContext setSession_id(String session_id) {
        this.session_id = session_id;
        return this;
    }

    public AuditContext setUname(String name) {
        this.uname = name;
        return this;
    }

    public String getUname() {
        return uname;
    }

    public String getOperation() {
        return operation;
    }

    public String getDetails() {
        return description_builder.toString();
    }


}
