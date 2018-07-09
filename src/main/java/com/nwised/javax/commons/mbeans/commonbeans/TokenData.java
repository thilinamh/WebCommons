package com.nwised.javax.commons.mbeans.commonbeans;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import javax.enterprise.context.RequestScoped;

/**
 * Created by Thilina on 3/25/2018.
 */
@RequestScoped
public class TokenData {
    private Jws<Claims> claimsJws;
    private Class constructed_class;

//    @Inject
//    private TokenData(InjectionPoint injectionPoint){
//        constructed_class =injectionPoint.getBean().getBeanClass();
//    }

    public Jws<Claims> getClaimsJws() {
        return claimsJws;
    }

    public void setClaimsJws(Jws<Claims> claimsJws) {
        this.claimsJws = claimsJws;
    }

    public String getSubject() {
        return claimsJws.getBody().getSubject();
    }

    public String getClaim(String key) {

        return claimsJws.getBody().get(key, String.class);
    }


}
