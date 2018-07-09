package com.nwised.javax.commons.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by tm on 7/14/17.
 */
public class CommonHttpUtils {
    public static Cookie getCookie(HttpServletRequest request, String cookie_name) {
        if (request.getCookies() != null) {
            for (Cookie ck : request.getCookies()) {
                if (ck.getName().equals(cookie_name)) {
                    return ck;
                }
            }
        }
        return null;
    }
}
