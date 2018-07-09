package com.nwised.javax.commons.jaxrs.filters.authentication;


import com.nwised.javax.commons.jaxrs.webexceptions.ForbiddenOperation;
import com.nwised.javax.commons.jaxrs.webexceptions.InvalidDataEx;
import com.nwised.javax.commons.mbeans.commonbeans.SessionData;
import com.nwised.javax.commons.mbeans.commonbeans.TokenData;
import com.nwised.javax.commons.jaxrs.utils.KeyHandler;
import io.jsonwebtoken.*;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NameBinding;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Thilina on 1/14/2018.
 */
@Provider
@Priority(Priorities.AUTHENTICATION - 100)
@JWTFilter.TokenAuthenticated
public class JWTFilter implements ContainerRequestFilter {

    @Context
    HttpHeaders headers;

    @Inject
    KeyHandler keyHandler;

    @Inject
    TokenData tokenData;

    @Inject
    private SessionData sessionData;

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(value = RetentionPolicy.RUNTIME)
    @NameBinding
    public @interface TokenAuthenticated {
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String token = headers.getHeaderString("token");
        try {

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(keyHandler.getJWTSigningKey())
                    .parseClaimsJws(token);
            sessionData.putData("jwt_claims", claimsJws);
            tokenData.setClaimsJws(claimsJws);

        } catch (SignatureException e) {
            throw new InvalidDataEx("Invalid Session. Please Re login");
        } catch (ExpiredJwtException ex) {
            containerRequestContext.abortWith(new ForbiddenOperation("Invalid Session").getResponse());
            return;
        }
    }
}
