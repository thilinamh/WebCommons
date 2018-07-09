/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nwised.javax.commons.exceptions;

/**
 * @author thilina_h
 */
public class SocketConnectionEx extends ExceptionBase {
    public interface SocketError {
        String getEcode();

        default String getEmessage() {
            return "";
        }
    }

    static final private String msg = "Error connecting socket";
    static final private String ecode = "serr";
    private String errCode;
    static private final int http_code = 503;
    private SocketError socketError;

//    public SwitchResponseEx(String commmessage) {
//        super(commmessage);
//    }

    public SocketConnectionEx() {
        super(msg, null, http_code, ecode);
    }

    public SocketConnectionEx(Throwable cause) {
        super(msg, cause, http_code, ecode);
    }

    public SocketConnectionEx(String msg, Throwable cause) {
        super(msg, cause, http_code, ecode);
    }


    public SocketConnectionEx(String message, String ecode) {
        super(message, null, http_code, ecode);
        errCode = ecode;
    }

    public SocketConnectionEx(String message, SocketError socketError) {
        super(message, null, http_code, socketError.getEcode());
        errCode = socketError.getEcode();
        this.socketError=socketError;

    }

    public SocketConnectionEx(SocketError socketError) {
        super(socketError.getEmessage(), null, http_code, socketError.getEcode());
        errCode = socketError.getEcode();
        this.socketError=socketError;
    }

    public SocketError getSocketError() {
        return socketError;
    }

    public String getErrCode() {
        return errCode;
    }

}
