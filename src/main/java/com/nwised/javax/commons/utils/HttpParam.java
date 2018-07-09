package com.nwised.javax.commons.utils;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by thilina_h on 4/27/2018.
 */
@Qualifier

@Retention(RUNTIME)

@Target({TYPE, METHOD, FIELD, PARAMETER})

public @interface HttpParam {

    @Nonbinding public String value();

}