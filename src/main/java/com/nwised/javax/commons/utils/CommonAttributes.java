package com.nwised.javax.commons.utils;

/**
 * Created by tm on 7/14/17.
 */
public enum CommonAttributes {
    TOKEN_SESSION("t_session");
    private String name;

    private CommonAttributes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
